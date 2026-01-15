package com.ryuqq.setof.migration.scheduler;

import com.ryuqq.setof.migration.config.MigrationProperties;
import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpoint;
import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import com.ryuqq.setof.migration.core.checkpoint.SyncMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 증분 동기화 스케줄러
 *
 * <p>각 도메인의 체크포인트를 주기적으로 확인하고, sync_interval_seconds에 따라 증분 동기화 Job을 자동 실행합니다.
 *
 * <h2>동작 원리</h2>
 *
 * <ul>
 *   <li>1분마다 모든 도메인 체크포인트 확인
 *   <li>INCREMENTAL 모드이고 syncIntervalSeconds가 지났으면 Job 실행
 *   <li>이미 실행 중인 도메인은 스킵
 *   <li>동시 실행 방지를 위한 락 관리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@ConditionalOnProperty(
        name = "migration.schedule.incremental-enabled",
        havingValue = "true",
        matchIfMissing = false)
public class IncrementalSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(IncrementalSyncScheduler.class);

    private final JobLauncher jobLauncher;
    private final MigrationCheckpointRepository checkpointRepository;
    private final MigrationProperties migrationProperties;

    /**
     * 도메인별 Job Bean 매핑
     *
     * <p>새 도메인 추가 시 이 Map에 Job Bean을 등록해야 합니다.
     */
    private final Map<String, Job> domainJobMap;

    /**
     * 동시 실행 방지를 위한 락
     *
     * <p>도메인별로 Job이 실행 중인지 추적합니다.
     */
    private final Map<String, Boolean> runningJobs = new ConcurrentHashMap<>();

    public IncrementalSyncScheduler(
            JobLauncher jobLauncher,
            MigrationCheckpointRepository checkpointRepository,
            MigrationProperties migrationProperties,
            @Qualifier("memberIncrementalSyncJob") Job memberIncrementalSyncJob) {
        this.jobLauncher = jobLauncher;
        this.checkpointRepository = checkpointRepository;
        this.migrationProperties = migrationProperties;

        // 도메인별 Job 매핑
        this.domainJobMap = new ConcurrentHashMap<>();
        this.domainJobMap.put("member", memberIncrementalSyncJob);
        // TODO: 추가 도메인 Job 등록
        // this.domainJobMap.put("product", productIncrementalSyncJob);
        // this.domainJobMap.put("order", orderIncrementalSyncJob);
    }

    /**
     * 증분 동기화 스케줄 실행
     *
     * <p>1분마다 실행되어 동기화가 필요한 도메인의 Job을 트리거합니다.
     */
    @Scheduled(fixedRateString = "${migration.schedule.check-interval-ms:60000}")
    public void checkAndTriggerIncrementalSync() {
        if (!migrationProperties.schedule().incrementalEnabled()) {
            return;
        }

        log.debug("Checking domains for incremental sync...");

        List<MigrationCheckpoint> checkpoints = checkpointRepository.findAll();

        for (MigrationCheckpoint checkpoint : checkpoints) {
            if (shouldTriggerSync(checkpoint)) {
                triggerIncrementalSync(checkpoint);
            }
        }
    }

    /**
     * 동기화를 트리거해야 하는지 판단
     *
     * @param checkpoint 체크포인트
     * @return 동기화가 필요하면 true
     */
    private boolean shouldTriggerSync(MigrationCheckpoint checkpoint) {
        String domainName = checkpoint.domainName();

        // 1. INCREMENTAL 모드인지 확인
        if (checkpoint.syncMode() != SyncMode.INCREMENTAL) {
            return false;
        }

        // 2. 해당 도메인의 Job이 등록되어 있는지 확인
        if (!domainJobMap.containsKey(domainName)) {
            log.warn("No incremental sync job registered for domain: {}", domainName);
            return false;
        }

        // 3. 이미 실행 중인지 확인
        if (Boolean.TRUE.equals(runningJobs.get(domainName))) {
            log.debug("Incremental sync already running for domain: {}", domainName);
            return false;
        }

        // 4. 현재 RUNNING 상태인지 확인
        if (checkpoint.isRunning()) {
            return false;
        }

        // 5. 마지막 동기화 이후 충분한 시간이 지났는지 확인
        Instant lastSyncedAt = checkpoint.lastSyncedAt();
        if (lastSyncedAt == null) {
            // 첫 증분 동기화
            return true;
        }

        int intervalSeconds = checkpoint.syncIntervalSeconds();
        if (intervalSeconds <= 0) {
            intervalSeconds = 300; // 기본값 5분
        }

        Instant nextSyncTime = lastSyncedAt.plusSeconds(intervalSeconds);
        return Instant.now().isAfter(nextSyncTime);
    }

    /**
     * 증분 동기화 Job 트리거
     *
     * @param checkpoint 체크포인트
     */
    private void triggerIncrementalSync(MigrationCheckpoint checkpoint) {
        String domainName = checkpoint.domainName();
        Job job = domainJobMap.get(domainName);

        if (job == null) {
            log.error("Job not found for domain: {}", domainName);
            return;
        }

        // 동시 실행 방지 락 획득
        if (runningJobs.putIfAbsent(domainName, true) != null) {
            log.debug("Another sync already started for domain: {}", domainName);
            return;
        }

        try {
            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .addString("domain", domainName)
                            .addString("mode", "incremental")
                            .addString("triggeredBy", "scheduler")
                            .toJobParameters();

            log.info(
                    "Triggering incremental sync for domain: {}, lastSyncedAt: {}, interval: {}s",
                    domainName,
                    checkpoint.lastSyncedAt(),
                    checkpoint.syncIntervalSeconds());

            // 비동기로 Job 실행
            jobLauncher.run(job, jobParameters);

            log.info("Incremental sync job started for domain: {}", domainName);

        } catch (Exception e) {
            log.error("Failed to trigger incremental sync for domain: {}", domainName, e);
        } finally {
            // 락 해제 (Job 완료 후 해제를 위해 JobListener에서 처리하는 것이 더 정확함)
            // 여기서는 시작 시 에러 발생 시에만 해제
            // 정상 완료는 IncrementalSyncCompletionCallback에서 처리
        }
    }

    /**
     * Job 완료 이벤트 처리 (락 해제)
     *
     * <p>IncrementalSyncCompletedEvent가 발행되면 해당 도메인의 락을 해제합니다.
     *
     * @param event 증분 동기화 완료 이벤트
     */
    @EventListener
    public void onIncrementalSyncCompleted(IncrementalSyncCompletedEvent event) {
        String domainName = event.getDomainName();
        runningJobs.remove(domainName);
        log.info("Lock released for domain: {}, success: {}", domainName, event.isSuccess());
    }

    /**
     * Job 완료 시 락 해제 (직접 호출용)
     *
     * @param domainName 도메인명
     */
    public void releaseLock(String domainName) {
        runningJobs.remove(domainName);
        log.debug("Lock released for domain: {}", domainName);
    }

    /**
     * 수동으로 특정 도메인의 동기화 트리거
     *
     * @param domainName 도메인명
     * @return 트리거 성공 여부
     */
    public boolean triggerManually(String domainName) {
        return checkpointRepository
                .findByDomainName(domainName)
                .map(
                        checkpoint -> {
                            if (checkpoint.syncMode() != SyncMode.INCREMENTAL) {
                                log.warn(
                                        "Cannot trigger manual sync: domain {} is not in"
                                                + " INCREMENTAL mode",
                                        domainName);
                                return false;
                            }
                            triggerIncrementalSync(checkpoint);
                            return true;
                        })
                .orElse(false);
    }
}

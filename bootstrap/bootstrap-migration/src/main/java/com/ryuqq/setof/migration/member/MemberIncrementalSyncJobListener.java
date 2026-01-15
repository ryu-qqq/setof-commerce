package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import com.ryuqq.setof.migration.scheduler.IncrementalSyncCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Member 증분 동기화 Job Listener
 *
 * <p>Job 시작/종료 시 로깅 및 체크포인트 상태 관리
 *
 * @author development-team
 * @since 1.0.0
 */
public class MemberIncrementalSyncJobListener implements JobExecutionListener {

    private static final Logger log =
            LoggerFactory.getLogger(MemberIncrementalSyncJobListener.class);
    private static final String DOMAIN_NAME = "member";

    private final MigrationCheckpointRepository checkpointRepository;
    private final ApplicationEventPublisher eventPublisher;
    private long startTimeMs;

    public MemberIncrementalSyncJobListener(
            MigrationCheckpointRepository checkpointRepository,
            ApplicationEventPublisher eventPublisher) {
        this.checkpointRepository = checkpointRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTimeMs = System.currentTimeMillis();
        log.info("========================================");
        log.info("Member Incremental Sync Job Started");
        log.info("Job ID: {}", jobExecution.getJobId());
        log.info("Job Instance ID: {}", jobExecution.getJobInstance().getInstanceId());
        log.info("========================================");

        // 체크포인트 상태 로깅
        checkpointRepository
                .findByDomainName(DOMAIN_NAME)
                .ifPresent(
                        cp -> {
                            log.info("Checkpoint Status:");
                            log.info("  - Sync Mode: {}", cp.syncMode());
                            log.info("  - Last Synced At: {}", cp.lastSyncedAt());
                            log.info("  - Migrated Count: {}", cp.migratedCount());
                            log.info("  - Skipped Count: {}", cp.skippedCount());
                            log.info("  - Failed Count: {}", cp.failedCount());
                        });
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long executionTimeMs = System.currentTimeMillis() - startTimeMs;
        BatchStatus status = jobExecution.getStatus();

        log.info("========================================");
        log.info("Member Incremental Sync Job Completed");
        log.info("Status: {}", status);
        log.info("Execution Time: {} ms ({} seconds)", executionTimeMs, executionTimeMs / 1000);

        if (status == BatchStatus.FAILED) {
            log.error("Job failed with exceptions:");
            jobExecution
                    .getAllFailureExceptions()
                    .forEach(e -> log.error("  - {}", e.getMessage()));
        }

        // 최종 체크포인트 상태 로깅
        checkpointRepository
                .findByDomainName(DOMAIN_NAME)
                .ifPresent(
                        cp -> {
                            log.info("Final Checkpoint Status:");
                            log.info("  - Last Synced At: {}", cp.lastSyncedAt());
                            log.info("  - Migrated Count: {}", cp.migratedCount());
                            log.info("  - Last Batch Size: {}", cp.lastBatchSize());
                            log.info("  - Skipped Count: {}", cp.skippedCount());
                            log.info("  - Failed Count: {}", cp.failedCount());
                        });

        log.info("========================================");

        // 스케줄러 락 해제를 위한 이벤트 발행
        boolean success = (status == BatchStatus.COMPLETED);
        if (eventPublisher != null) {
            eventPublisher.publishEvent(
                    new IncrementalSyncCompletedEvent(this, DOMAIN_NAME, success));
        }
    }
}

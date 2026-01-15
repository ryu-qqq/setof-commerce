package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpoint;
import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import com.ryuqq.setof.migration.core.checkpoint.SyncMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member 마이그레이션 Job 실행 컨트롤러
 *
 * <p>Member 데이터 마이그레이션 Job을 실행하고 상태를 조회하는 REST API를 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/migration/member")
public class MemberMigrationJobController {

    private static final Logger log = LoggerFactory.getLogger(MemberMigrationJobController.class);
    private static final String DOMAIN_NAME = "member";
    private static final String JOB_NAME = "memberMigrationJob";
    private static final String INCREMENTAL_JOB_NAME = "memberIncrementalSyncJob";

    private final JobLauncher jobLauncher;
    private final Job memberMigrationJob;
    private final Job memberIncrementalSyncJob;
    private final JobExplorer jobExplorer;
    private final MigrationCheckpointRepository checkpointRepository;

    public MemberMigrationJobController(
            JobLauncher jobLauncher,
            @Qualifier("memberMigrationJob") Job memberMigrationJob,
            @Qualifier("memberIncrementalSyncJob") Job memberIncrementalSyncJob,
            JobExplorer jobExplorer,
            MigrationCheckpointRepository checkpointRepository) {
        this.jobLauncher = jobLauncher;
        this.memberMigrationJob = memberMigrationJob;
        this.memberIncrementalSyncJob = memberIncrementalSyncJob;
        this.jobExplorer = jobExplorer;
        this.checkpointRepository = checkpointRepository;
    }

    /**
     * Member 마이그레이션 Job 실행
     *
     * @return Job 실행 결과
     */
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runMigrationJob() {
        log.info("Member migration job execution requested");

        Map<String, Object> response = new HashMap<>();

        // 이미 실행 중인지 확인
        Optional<MigrationCheckpoint> existingCheckpoint =
                checkpointRepository.findByDomainName(DOMAIN_NAME);
        if (existingCheckpoint.isPresent() && existingCheckpoint.get().isRunning()) {
            log.warn("Member migration job is already running");
            response.put("success", false);
            response.put("message", "Migration job is already running");
            response.put("checkpoint", existingCheckpoint.get());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .addString("domain", DOMAIN_NAME)
                            .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(memberMigrationJob, jobParameters);

            response.put("success", true);
            response.put("jobExecutionId", jobExecution.getId());
            response.put("status", jobExecution.getStatus().toString());
            response.put("startTime", jobExecution.getStartTime());
            response.put("message", "Migration job started successfully");

            log.info("Member migration job started: executionId={}", jobExecution.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to start member migration job", e);
            response.put("success", false);
            response.put("message", "Failed to start migration job: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Member 마이그레이션 상태 조회
     *
     * @return 마이그레이션 상태 정보
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getMigrationStatus() {
        Map<String, Object> response = new HashMap<>();

        // 체크포인트 정보 조회
        Optional<MigrationCheckpoint> checkpoint =
                checkpointRepository.findByDomainName(DOMAIN_NAME);

        if (checkpoint.isEmpty()) {
            response.put("domainName", DOMAIN_NAME);
            response.put("status", "NOT_INITIALIZED");
            response.put("message", "Migration checkpoint not found");
            return ResponseEntity.ok(response);
        }

        MigrationCheckpoint cp = checkpoint.get();
        response.put("domainName", cp.domainName());
        response.put("status", cp.status());
        response.put("lastMigratedId", cp.lastMigratedId());
        response.put("migratedCount", cp.migratedCount());
        response.put("legacyTotalCount", cp.legacyTotalCount());
        response.put("progressPercentage", cp.progressPercentage());
        response.put("lastExecutedAt", cp.lastExecutedAt());
        response.put("lastCompletedAt", cp.lastCompletedAt());
        response.put("executionTimeMs", cp.executionTimeMs());
        response.put("errorMessage", cp.errorMessage());

        // 증분 동기화 정보
        response.put("syncMode", cp.syncMode());
        response.put("lastSyncedAt", cp.lastSyncedAt());
        response.put("lastBatchSize", cp.lastBatchSize());
        response.put("skippedCount", cp.skippedCount());
        response.put("failedCount", cp.failedCount());

        // 최근 Job 실행 정보 조회
        try {
            var lastJobInstance = jobExplorer.getLastJobInstance(JOB_NAME);
            if (lastJobInstance != null) {
                var lastExecution = jobExplorer.getLastJobExecution(lastJobInstance);
                if (lastExecution != null) {
                    response.put("lastJobExecutionId", lastExecution.getId());
                    response.put("lastJobStatus", lastExecution.getStatus().toString());
                    response.put("lastJobStartTime", lastExecution.getStartTime());
                    response.put("lastJobEndTime", lastExecution.getEndTime());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to get last job execution info", e);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Member 마이그레이션 체크포인트 초기화 (재시작용)
     *
     * @return 초기화 결과
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetCheckpoint() {
        log.info("Member migration checkpoint reset requested");

        Map<String, Object> response = new HashMap<>();

        // 실행 중인지 확인
        Optional<MigrationCheckpoint> existingCheckpoint =
                checkpointRepository.findByDomainName(DOMAIN_NAME);
        if (existingCheckpoint.isPresent() && existingCheckpoint.get().isRunning()) {
            log.warn("Cannot reset checkpoint while migration is running");
            response.put("success", false);
            response.put("message", "Cannot reset checkpoint while migration is running");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            checkpointRepository.resetCheckpoint(DOMAIN_NAME);
            response.put("success", true);
            response.put("message", "Checkpoint reset successfully");
            response.put("domainName", DOMAIN_NAME);
            response.put("resetAt", LocalDateTime.now());

            log.info("Member migration checkpoint reset completed");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to reset checkpoint", e);
            response.put("success", false);
            response.put("message", "Failed to reset checkpoint: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Member 증분 동기화 Job 실행
     *
     * <p>초기 마이그레이션 완료 후 변경된 데이터만 동기화합니다.
     *
     * @return Job 실행 결과
     */
    @PostMapping("/incremental/run")
    public ResponseEntity<Map<String, Object>> runIncrementalSyncJob() {
        log.info("Member incremental sync job execution requested");

        Map<String, Object> response = new HashMap<>();

        // 체크포인트 확인
        Optional<MigrationCheckpoint> existingCheckpoint =
                checkpointRepository.findByDomainName(DOMAIN_NAME);

        if (existingCheckpoint.isEmpty()) {
            response.put("success", false);
            response.put("message", "Migration checkpoint not found. Run initial migration first.");
            return ResponseEntity.badRequest().body(response);
        }

        MigrationCheckpoint checkpoint = existingCheckpoint.get();

        // 실행 중인지 확인
        if (checkpoint.isRunning()) {
            log.warn("A migration job is already running");
            response.put("success", false);
            response.put("message", "A migration job is already running");
            response.put("checkpoint", checkpoint);
            return ResponseEntity.badRequest().body(response);
        }

        // INCREMENTAL 모드인지 확인
        if (checkpoint.syncMode() != SyncMode.INCREMENTAL) {
            log.warn("Not in incremental mode. Current mode: {}", checkpoint.syncMode());
            response.put("success", false);
            response.put(
                    "message",
                    "Not in incremental sync mode. Switch to incremental mode first or run initial"
                            + " migration.");
            response.put("currentMode", checkpoint.syncMode());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .addString("domain", DOMAIN_NAME)
                            .addString("mode", "incremental")
                            .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(memberIncrementalSyncJob, jobParameters);

            response.put("success", true);
            response.put("jobExecutionId", jobExecution.getId());
            response.put("status", jobExecution.getStatus().toString());
            response.put("startTime", jobExecution.getStartTime());
            response.put("message", "Incremental sync job started successfully");

            log.info("Member incremental sync job started: executionId={}", jobExecution.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to start member incremental sync job", e);
            response.put("success", false);
            response.put("message", "Failed to start incremental sync job: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 증분 동기화 모드로 전환
     *
     * <p>초기 마이그레이션 완료 후 증분 동기화 모드로 전환합니다.
     *
     * @return 전환 결과
     */
    @PostMapping("/switch-to-incremental")
    public ResponseEntity<Map<String, Object>> switchToIncrementalMode() {
        log.info("Switching to incremental sync mode requested");

        Map<String, Object> response = new HashMap<>();

        Optional<MigrationCheckpoint> existingCheckpoint =
                checkpointRepository.findByDomainName(DOMAIN_NAME);

        if (existingCheckpoint.isEmpty()) {
            response.put("success", false);
            response.put("message", "Migration checkpoint not found. Run initial migration first.");
            return ResponseEntity.badRequest().body(response);
        }

        MigrationCheckpoint checkpoint = existingCheckpoint.get();

        // 이미 INCREMENTAL 모드인 경우
        if (checkpoint.syncMode() == SyncMode.INCREMENTAL) {
            response.put("success", true);
            response.put("message", "Already in incremental sync mode");
            response.put("syncMode", checkpoint.syncMode());
            response.put("lastSyncedAt", checkpoint.lastSyncedAt());
            return ResponseEntity.ok(response);
        }

        // 실행 중인 경우 전환 불가
        if (checkpoint.isRunning()) {
            response.put("success", false);
            response.put("message", "Cannot switch mode while migration is running");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            checkpointRepository.switchToIncrementalMode(DOMAIN_NAME);

            response.put("success", true);
            response.put("message", "Switched to incremental sync mode");
            response.put("previousMode", checkpoint.syncMode());
            response.put("newMode", SyncMode.INCREMENTAL);
            response.put("switchedAt", LocalDateTime.now());

            log.info("Switched to incremental sync mode for domain: {}", DOMAIN_NAME);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to switch to incremental mode", e);
            response.put("success", false);
            response.put("message", "Failed to switch mode: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}

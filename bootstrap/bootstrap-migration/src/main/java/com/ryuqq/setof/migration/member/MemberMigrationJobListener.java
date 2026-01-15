package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Member 마이그레이션 Job 리스너
 *
 * <p>Job 시작/종료 시점에 체크포인트 상태를 관리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class MemberMigrationJobListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(MemberMigrationJobListener.class);
    private static final String DOMAIN_NAME = "member";

    private final MigrationCheckpointRepository checkpointRepository;
    private final LegacyUserRepository legacyUserRepository;
    private long startTimeMs;

    public MemberMigrationJobListener(
            MigrationCheckpointRepository checkpointRepository,
            LegacyUserRepository legacyUserRepository) {
        this.checkpointRepository = checkpointRepository;
        this.legacyUserRepository = legacyUserRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTimeMs = System.currentTimeMillis();
        log.info("========================================");
        log.info("Member Migration Job STARTED");
        log.info("========================================");

        // 레거시 전체 건수 조회 및 마이그레이션 시작
        long legacyTotalCount = legacyUserRepository.countAllUsers();
        checkpointRepository.startMigration(DOMAIN_NAME, legacyTotalCount);

        log.info("Legacy total count: {}", legacyTotalCount);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long executionTimeMs = System.currentTimeMillis() - startTimeMs;

        log.info("========================================");
        log.info("Member Migration Job FINISHED");
        log.info("Status: {}", jobExecution.getStatus());
        log.info("Execution Time: {} ms ({} seconds)", executionTimeMs, executionTimeMs / 1000);
        log.info("========================================");

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            checkpointRepository.completeMigration(DOMAIN_NAME, executionTimeMs);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            String errorMessage = extractErrorMessage(jobExecution);
            checkpointRepository.failMigration(DOMAIN_NAME, errorMessage);
        }
    }

    private String extractErrorMessage(JobExecution jobExecution) {
        return jobExecution.getAllFailureExceptions().stream()
                .findFirst()
                .map(Throwable::getMessage)
                .orElse("Unknown error");
    }
}

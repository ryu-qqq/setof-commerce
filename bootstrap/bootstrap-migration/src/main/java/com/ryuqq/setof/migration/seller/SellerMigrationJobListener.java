package com.ryuqq.setof.migration.seller;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Seller 마이그레이션 Job Listener
 *
 * <p>Job 시작/종료 시 체크포인트를 관리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class SellerMigrationJobListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(SellerMigrationJobListener.class);
    private static final String DOMAIN_NAME = "seller";

    private final MigrationCheckpointRepository checkpointRepository;
    private final LegacySellerRepository legacySellerRepository;
    private long startTimeMs;

    public SellerMigrationJobListener(
            MigrationCheckpointRepository checkpointRepository,
            LegacySellerRepository legacySellerRepository) {
        this.checkpointRepository = checkpointRepository;
        this.legacySellerRepository = legacySellerRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Starting Seller Migration Job: {}", jobExecution.getJobId());
        startTimeMs = System.currentTimeMillis();

        // 전체 셀러 수 조회 후 마이그레이션 시작
        long totalCount = legacySellerRepository.countAllSellers();
        checkpointRepository.startMigration(DOMAIN_NAME, totalCount);

        log.info("Seller migration started. Total sellers: {}", totalCount);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        var status = jobExecution.getStatus();
        long executionTimeMs = System.currentTimeMillis() - startTimeMs;
        log.info("Seller Migration Job completed with status: {}", status);

        if (status.isUnsuccessful()) {
            String errorMessage =
                    jobExecution.getAllFailureExceptions().isEmpty()
                            ? "Unknown error"
                            : jobExecution.getAllFailureExceptions().get(0).getMessage();
            checkpointRepository.failMigration(DOMAIN_NAME, errorMessage);
        } else {
            // 남은 데이터가 있는지 확인
            var checkpoint = checkpointRepository.findByDomainName(DOMAIN_NAME);
            if (checkpoint.isPresent()) {
                long remaining =
                        legacySellerRepository.countSellersAfter(checkpoint.get().lastMigratedId());
                if (remaining == 0) {
                    checkpointRepository.completeMigration(DOMAIN_NAME, executionTimeMs);
                    log.info(
                            "Seller migration completed successfully! executionTimeMs={}",
                            executionTimeMs);
                } else {
                    checkpointRepository.pauseMigration(DOMAIN_NAME);
                    log.info("Seller migration paused. Remaining: {}", remaining);
                }
            }
        }
    }
}

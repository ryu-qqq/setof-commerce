package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Member 마이그레이션 Step 리스너
 *
 * <p>Step 진행 상황을 모니터링하고 로깅합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class MemberMigrationStepListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(MemberMigrationStepListener.class);
    private static final String DOMAIN_NAME = "member";

    private final MigrationCheckpointRepository checkpointRepository;

    public MemberMigrationStepListener(MigrationCheckpointRepository checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step [{}] started", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("----------------------------------------");
        log.info("Step [{}] completed", stepExecution.getStepName());
        log.info("Read count: {}", stepExecution.getReadCount());
        log.info("Write count: {}", stepExecution.getWriteCount());
        log.info("Skip count: {}", stepExecution.getSkipCount());
        log.info("Commit count: {}", stepExecution.getCommitCount());
        log.info("Rollback count: {}", stepExecution.getRollbackCount());
        log.info("----------------------------------------");

        // 현재 체크포인트 상태 조회 및 진행률 로깅
        checkpointRepository
                .findByDomainName(DOMAIN_NAME)
                .ifPresent(
                        cp -> {
                            double progress = cp.progressPercentage();
                            if (progress >= 0) {
                                log.info(
                                        "Progress: {}/{} ({:.2f}%)",
                                        cp.migratedCount(), cp.legacyTotalCount(), progress);
                            }
                        });

        return stepExecution.getExitStatus();
    }
}

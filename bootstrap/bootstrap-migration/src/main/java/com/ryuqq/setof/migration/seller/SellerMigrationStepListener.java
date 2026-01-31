package com.ryuqq.setof.migration.seller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Seller 마이그레이션 Step Listener
 *
 * <p>Step 실행 전후로 로깅을 수행합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class SellerMigrationStepListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(SellerMigrationStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Starting seller migration step");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long readCount = stepExecution.getReadCount();
        long writeCount = stepExecution.getWriteCount();
        long skipCount = stepExecution.getSkipCount();

        log.info(
                "Seller migration step completed - Read: {}, Write: {}, Skip: {}",
                readCount,
                writeCount,
                skipCount);

        return stepExecution.getExitStatus();
    }
}

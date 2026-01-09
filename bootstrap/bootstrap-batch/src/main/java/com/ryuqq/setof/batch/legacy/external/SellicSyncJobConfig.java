package com.ryuqq.setof.batch.legacy.external;

import com.ryuqq.setof.batch.legacy.external.client.SellicClient;
import com.ryuqq.setof.batch.legacy.external.client.SpringAdminClient;
import com.ryuqq.setof.batch.legacy.external.dto.SellicOrder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * SELLIC 외부몰 주문 동기화 배치 Job 설정
 *
 * <p>SELLIC API에서 주문 데이터를 조회하여 Spring Admin API로 전송합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class SellicSyncJobConfig {

    private static final Logger log = LoggerFactory.getLogger(SellicSyncJobConfig.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SellicClient sellicClient;
    private final SpringAdminClient springAdminClient;

    public SellicSyncJobConfig(
            JobRepository jobRepository,
            @Qualifier("legacyTransactionManager") PlatformTransactionManager transactionManager,
            SellicClient sellicClient,
            SpringAdminClient springAdminClient) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.sellicClient = sellicClient;
        this.springAdminClient = springAdminClient;
    }

    @Bean
    public Job syncSellicOrderJob() {
        return new JobBuilder("syncSellicOrderJob", jobRepository)
                .start(syncSellicOrderStep())
                .build();
    }

    @Bean
    public Step syncSellicOrderStep() {
        return new StepBuilder("syncSellicOrderStep", jobRepository)
                .tasklet(syncSellicOrderTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet syncSellicOrderTasklet() {
        return (contribution, chunkContext) -> {
            log.info("Starting SELLIC order sync...");

            List<SellicOrder> orders = sellicClient.fetchOrders();
            log.info("Fetched {} orders from SELLIC", orders.size());

            if (orders.isEmpty()) {
                log.info("No orders to sync");
                return RepeatStatus.FINISHED;
            }

            int successCount = 0;
            int failCount = 0;

            for (SellicOrder order : orders) {
                try {
                    springAdminClient.sendOrder(order);
                    successCount++;
                } catch (Exception e) {
                    log.error(
                            "Failed to sync order: idx={}, error={}",
                            order.getIdx(),
                            e.getMessage());
                    failCount++;
                }
            }

            log.info("SELLIC order sync completed: success={}, fail={}", successCount, failCount);
            return RepeatStatus.FINISHED;
        };
    }
}

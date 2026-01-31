package com.ryuqq.setof.migration.seller;

import com.ryuqq.setof.migration.config.MigrationProperties;
import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Seller 마이그레이션 Job 설정
 *
 * <p>레거시 seller, seller_business_info, seller_shipping_info 테이블 → 신규 seller_applications 테이블 마이그레이션
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class SellerMigrationJobConfig {

    private static final Logger log = LoggerFactory.getLogger(SellerMigrationJobConfig.class);
    private static final String DOMAIN_NAME = "seller";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager batchTransactionManager;
    private final MigrationProperties migrationProperties;
    private final MigrationCheckpointRepository checkpointRepository;
    private final LegacySellerRepository legacySellerRepository;
    private final SellerMigrationRepository sellerMigrationRepository;

    public SellerMigrationJobConfig(
            JobRepository jobRepository,
            @Qualifier("batchTransactionManager")
                    PlatformTransactionManager batchTransactionManager,
            MigrationProperties migrationProperties,
            MigrationCheckpointRepository checkpointRepository,
            LegacySellerRepository legacySellerRepository,
            SellerMigrationRepository sellerMigrationRepository) {
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
        this.migrationProperties = migrationProperties;
        this.checkpointRepository = checkpointRepository;
        this.legacySellerRepository = legacySellerRepository;
        this.sellerMigrationRepository = sellerMigrationRepository;
    }

    /** Seller 마이그레이션 Job */
    @Bean
    public Job sellerMigrationJob() {
        return new JobBuilder("sellerMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(
                        new SellerMigrationJobListener(
                                checkpointRepository, legacySellerRepository))
                .start(sellerMigrationStep())
                .build();
    }

    /** Seller 마이그레이션 Step */
    @Bean
    public Step sellerMigrationStep() {
        return new StepBuilder("sellerMigrationStep", jobRepository)
                .<LegacySellerDto, SellerApplicationMigrationData>chunk(
                        migrationProperties.batch().chunkSize(), batchTransactionManager)
                .reader(sellerItemReader())
                .processor(sellerItemProcessor())
                .writer(sellerItemWriter())
                .faultTolerant()
                .skipLimit(migrationProperties.batch().skipLimit())
                .skip(Exception.class)
                .retryLimit(migrationProperties.batch().retryLimit())
                .retry(Exception.class)
                .listener(new SellerMigrationStepListener())
                .build();
    }

    /** ItemReader - 레거시 DB에서 셀러 조회 (PK 기반) */
    @Bean
    @StepScope
    public ItemReader<LegacySellerDto> sellerItemReader() {
        return new ItemReader<>() {
            private Iterator<LegacySellerDto> currentChunk;
            private long lastReadId = -1;
            private boolean initialized = false;

            @Override
            public LegacySellerDto read() {
                if (!initialized) {
                    initializeLastReadId();
                    initialized = true;
                }

                if (currentChunk == null || !currentChunk.hasNext()) {
                    List<LegacySellerDto> sellers =
                            legacySellerRepository.findSellersAfterIdOrdered(
                                    lastReadId, migrationProperties.batch().chunkSize());

                    if (sellers.isEmpty()) {
                        return null;
                    }

                    currentChunk = sellers.iterator();
                    log.debug("Fetched {} sellers after id {}", sellers.size(), lastReadId);
                }

                if (currentChunk.hasNext()) {
                    LegacySellerDto seller = currentChunk.next();
                    lastReadId = seller.sellerId();
                    return seller;
                }

                return null;
            }

            private void initializeLastReadId() {
                checkpointRepository
                        .findByDomainName(DOMAIN_NAME)
                        .ifPresent(cp -> lastReadId = cp.lastMigratedId());
                log.info("Initialized lastReadId: {}", lastReadId);
            }
        };
    }

    /** ItemProcessor - 레거시 데이터 → 신규 데이터 변환 */
    @Bean
    public ItemProcessor<LegacySellerDto, SellerApplicationMigrationData> sellerItemProcessor() {
        return new SellerItemProcessor();
    }

    /** ItemWriter - 신규 DB에 저장 */
    @Bean
    public ItemWriter<SellerApplicationMigrationData> sellerItemWriter() {
        return new SellerItemWriter(sellerMigrationRepository, checkpointRepository);
    }
}

package com.ryuqq.setof.migration.member;

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
 * Member 마이그레이션 Job 설정
 *
 * <p>레거시 USERS 테이블 → 신규 members 테이블 마이그레이션
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class MemberMigrationJobConfig {

    private static final Logger log = LoggerFactory.getLogger(MemberMigrationJobConfig.class);
    private static final String DOMAIN_NAME = "member";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager batchTransactionManager;
    private final MigrationProperties migrationProperties;
    private final MigrationCheckpointRepository checkpointRepository;
    private final LegacyUserRepository legacyUserRepository;
    private final MemberMigrationRepository memberMigrationRepository;

    public MemberMigrationJobConfig(
            JobRepository jobRepository,
            @Qualifier("batchTransactionManager")
                    PlatformTransactionManager batchTransactionManager,
            MigrationProperties migrationProperties,
            MigrationCheckpointRepository checkpointRepository,
            LegacyUserRepository legacyUserRepository,
            MemberMigrationRepository memberMigrationRepository) {
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
        this.migrationProperties = migrationProperties;
        this.checkpointRepository = checkpointRepository;
        this.legacyUserRepository = legacyUserRepository;
        this.memberMigrationRepository = memberMigrationRepository;
    }

    /** Member 마이그레이션 Job */
    @Bean
    public Job memberMigrationJob() {
        return new JobBuilder("memberMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(
                        new MemberMigrationJobListener(checkpointRepository, legacyUserRepository))
                .start(memberMigrationStep())
                .build();
    }

    /** Member 마이그레이션 Step */
    @Bean
    public Step memberMigrationStep() {
        return new StepBuilder("memberMigrationStep", jobRepository)
                .<LegacyUserDto, MemberMigrationData>chunk(
                        migrationProperties.batch().chunkSize(), batchTransactionManager)
                .reader(memberItemReader())
                .processor(memberItemProcessor())
                .writer(memberItemWriter())
                .faultTolerant()
                .skipLimit(migrationProperties.batch().skipLimit())
                .skip(Exception.class)
                .retryLimit(migrationProperties.batch().retryLimit())
                .retry(Exception.class)
                .listener(new MemberMigrationStepListener(checkpointRepository))
                .build();
    }

    /** ItemReader - 레거시 DB에서 사용자 조회 (PK 기반) */
    @Bean
    @StepScope
    public ItemReader<LegacyUserDto> memberItemReader() {
        return new ItemReader<>() {
            private Iterator<LegacyUserDto> currentChunk;
            private long lastReadId = -1;
            private boolean initialized = false;

            @Override
            public LegacyUserDto read() {
                if (!initialized) {
                    initializeLastReadId();
                    initialized = true;
                }

                if (currentChunk == null || !currentChunk.hasNext()) {
                    List<LegacyUserDto> users =
                            legacyUserRepository.findUsersAfterIdOrdered(
                                    lastReadId, migrationProperties.batch().chunkSize());

                    if (users.isEmpty()) {
                        return null;
                    }

                    currentChunk = users.iterator();
                    log.debug("Fetched {} users after id {}", users.size(), lastReadId);
                }

                if (currentChunk.hasNext()) {
                    LegacyUserDto user = currentChunk.next();
                    lastReadId = user.userId();
                    return user;
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
    public ItemProcessor<LegacyUserDto, MemberMigrationData> memberItemProcessor() {
        return new MemberItemProcessor();
    }

    /** ItemWriter - 신규 DB에 저장 */
    @Bean
    public ItemWriter<MemberMigrationData> memberItemWriter() {
        return new MemberItemWriter(memberMigrationRepository, checkpointRepository);
    }
}

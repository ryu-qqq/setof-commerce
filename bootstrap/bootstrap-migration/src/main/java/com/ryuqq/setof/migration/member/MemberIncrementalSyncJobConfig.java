package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.config.MigrationProperties;
import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Member 증분 동기화 Job 설정
 *
 * <p>초기 마이그레이션 완료 후, UPDATE_DATE 기반으로 변경된 데이터만 동기화합니다.
 *
 * <p>특징:
 *
 * <ul>
 *   <li>UPDATE_DATE > last_synced_at 기준으로 변경된 데이터 조회
 *   <li>UPSERT 로직 (기존 데이터 있으면 UPDATE, 없으면 INSERT)
 *   <li>실패 시 개별 레코드 스킵 (전체 롤백 아님)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class MemberIncrementalSyncJobConfig {

    private static final Logger log = LoggerFactory.getLogger(MemberIncrementalSyncJobConfig.class);
    private static final String DOMAIN_NAME = "member";
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final JobRepository jobRepository;
    private final PlatformTransactionManager batchTransactionManager;
    private final MigrationProperties migrationProperties;
    private final MigrationCheckpointRepository checkpointRepository;
    private final LegacyUserRepository legacyUserRepository;
    private final MemberMigrationRepository memberMigrationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MemberIncrementalSyncJobConfig(
            JobRepository jobRepository,
            @Qualifier("batchTransactionManager")
                    PlatformTransactionManager batchTransactionManager,
            MigrationProperties migrationProperties,
            MigrationCheckpointRepository checkpointRepository,
            LegacyUserRepository legacyUserRepository,
            MemberMigrationRepository memberMigrationRepository,
            ApplicationEventPublisher eventPublisher) {
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
        this.migrationProperties = migrationProperties;
        this.checkpointRepository = checkpointRepository;
        this.legacyUserRepository = legacyUserRepository;
        this.memberMigrationRepository = memberMigrationRepository;
        this.eventPublisher = eventPublisher;
    }

    /** Member 증분 동기화 Job */
    @Bean
    public Job memberIncrementalSyncJob() {
        return new JobBuilder("memberIncrementalSyncJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(
                        new MemberIncrementalSyncJobListener(checkpointRepository, eventPublisher))
                .start(memberIncrementalSyncStep())
                .build();
    }

    /** Member 증분 동기화 Step */
    @Bean
    public Step memberIncrementalSyncStep() {
        return new StepBuilder("memberIncrementalSyncStep", jobRepository)
                .<LegacyUserDto, MemberMigrationData>chunk(
                        migrationProperties.batch().chunkSize(), batchTransactionManager)
                .reader(memberIncrementalSyncItemReader())
                .processor(memberIncrementalSyncItemProcessor())
                .writer(memberIncrementalSyncItemWriter())
                .faultTolerant()
                .skipLimit(migrationProperties.batch().skipLimit())
                .skip(Exception.class)
                .retryLimit(migrationProperties.batch().retryLimit())
                .retry(Exception.class)
                .build();
    }

    /** ItemReader - 레거시 DB에서 수정된 사용자 조회 (UPDATE_DATE 기반) */
    @Bean
    @StepScope
    public ItemReader<LegacyUserDto> memberIncrementalSyncItemReader() {
        return new ItemReader<>() {
            private Iterator<LegacyUserDto> currentChunk;
            private LocalDateTime lastSyncedAt;
            private boolean initialized = false;
            private boolean completed = false;

            @Override
            public LegacyUserDto read() {
                if (completed) {
                    return null;
                }

                if (!initialized) {
                    initializeLastSyncedAt();
                    initialized = true;
                }

                if (currentChunk == null || !currentChunk.hasNext()) {
                    List<LegacyUserDto> users =
                            legacyUserRepository.findUsersModifiedAfter(
                                    lastSyncedAt, migrationProperties.batch().chunkSize());

                    if (users.isEmpty()) {
                        completed = true;
                        return null;
                    }

                    // 다음 청크 조회를 위해 마지막 아이템의 updatedAt 기록
                    LegacyUserDto lastUser = users.get(users.size() - 1);
                    if (lastUser.updatedAt() != null) {
                        lastSyncedAt = lastUser.updatedAt();
                    }

                    currentChunk = users.iterator();
                    log.debug("Fetched {} modified users after {}", users.size(), lastSyncedAt);
                }

                if (currentChunk.hasNext()) {
                    return currentChunk.next();
                }

                return null;
            }

            private void initializeLastSyncedAt() {
                // 체크포인트에서 마지막 동기화 시점 조회
                lastSyncedAt =
                        checkpointRepository
                                .findLastSyncedAt(DOMAIN_NAME)
                                .map(instant -> LocalDateTime.ofInstant(instant, KST))
                                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0, 0));

                log.info("Initialized incremental sync from: {}", lastSyncedAt);
            }
        };
    }

    /** ItemProcessor - 증분 동기화에서도 동일한 변환 로직 사용 */
    @Bean
    public ItemProcessor<LegacyUserDto, MemberMigrationData> memberIncrementalSyncItemProcessor() {
        return new MemberItemProcessor();
    }

    /** ItemWriter - 증분 동기화용 UPSERT Writer */
    @Bean
    public ItemWriter<MemberMigrationData> memberIncrementalSyncItemWriter() {
        return new MemberIncrementalSyncItemWriter(memberMigrationRepository, checkpointRepository);
    }
}

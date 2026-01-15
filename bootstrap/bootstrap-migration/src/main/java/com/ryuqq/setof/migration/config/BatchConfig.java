package com.ryuqq.setof.migration.config;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch 공통 설정
 *
 * <p>Spring Batch의 핵심 인프라 빈들을 설정합니다. 메타 데이터는 migrationDataSource에 저장됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final DataSource migrationDataSource;

    public BatchConfig(@Qualifier("migrationDataSource") DataSource migrationDataSource) {
        this.migrationDataSource = migrationDataSource;
    }

    /**
     * Batch 전용 TransactionManager
     *
     * <p>Spring Batch 메타 테이블 관리용 트랜잭션 매니저입니다.
     */
    @Bean(name = "batchTransactionManager")
    public PlatformTransactionManager batchTransactionManager() {
        return new DataSourceTransactionManager(migrationDataSource);
    }

    /**
     * JobRepository
     *
     * <p>배치 Job 실행 상태를 저장하는 저장소입니다. migrationDataSource의 BATCH_* 테이블에 저장됩니다.
     */
    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(migrationDataSource);
        factory.setTransactionManager(batchTransactionManager());
        factory.setDatabaseType("MYSQL");
        factory.setTablePrefix("BATCH_");
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * JobExplorer
     *
     * <p>배치 Job 실행 이력을 조회하는 탐색기입니다.
     */
    @Bean
    public JobExplorer jobExplorer() throws Exception {
        JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
        factory.setDataSource(migrationDataSource);
        factory.setTablePrefix("BATCH_");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * JobLauncher
     *
     * <p>배치 Job을 실행하는 런처입니다. 비동기 실행을 지원합니다.
     */
    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}

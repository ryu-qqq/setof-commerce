package com.ryuqq.setof.batch.config;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch 기본 설정
 *
 * <p>legacyDataSource를 사용하여 Spring Batch 메타 테이블을 관리합니다. 레거시 배치 작업이므로 레거시 DB(luxurydb)에 메타 테이블을
 * 저장합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

    private final DataSource legacyDataSource;

    public BatchConfig(@Qualifier("legacyDataSource") DataSource legacyDataSource) {
        this.legacyDataSource = legacyDataSource;
    }

    @Override
    protected DataSource getDataSource() {
        return legacyDataSource;
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return new DataSourceTransactionManager(legacyDataSource);
    }
}

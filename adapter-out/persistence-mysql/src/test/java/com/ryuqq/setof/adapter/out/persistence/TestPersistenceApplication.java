package com.ryuqq.setof.adapter.out.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TestPersistenceApplication - 테스트용 Spring Boot Application
 *
 * <p>Persistence Layer 통합 테스트에서 사용되는 테스트용 Application 클래스입니다.
 *
 * <p>@SpringBootTest에서 자동으로 이 클래스를 찾아 컨텍스트를 구성합니다.
 *
 * <p>JPA Repository 및 Entity 스캔은 JpaConfig에서 설정됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.ryuqq.setof.adapter.out.persistence")
public class TestPersistenceApplication {
    // 테스트 전용 부트스트랩 클래스
}

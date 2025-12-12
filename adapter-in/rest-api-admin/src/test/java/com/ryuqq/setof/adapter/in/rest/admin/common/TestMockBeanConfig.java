package com.ryuqq.setof.adapter.in.rest.admin.common;

import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Admin API 통합 테스트용 Mock Bean 설정
 *
 * <p>외부 어댑터를 Mock으로 대체합니다.
 *
 * <p><strong>테스트 인증 지원:</strong>
 *
 * <ul>
 *   <li>Admin API는 별도의 인증 메커니즘을 사용합니다 (Basic Auth, API Key 등)
 *   <li>{@code TEST_ADMIN_TOKEN_PREFIX + adminId} 형식의 토큰을 유효한 토큰으로 인식
 *   <li>예: "TEST_ADMIN_TOKEN_1" → adminId = "1"
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 * @see ApiIntegrationTestSupport
 */
@TestConfiguration
public class TestMockBeanConfig {

    /** Admin 테스트 토큰 접두사 */
    public static final String TEST_ADMIN_TOKEN_PREFIX = "TEST_ADMIN_TOKEN_";

    /** 기본 테스트 관리자 ID */
    public static final Long DEFAULT_TEST_ADMIN_ID = 1L;

    @Bean
    public ClockHolder clockHolder() {
        return () -> Clock.system(ZoneId.of("Asia/Seoul"));
    }
}

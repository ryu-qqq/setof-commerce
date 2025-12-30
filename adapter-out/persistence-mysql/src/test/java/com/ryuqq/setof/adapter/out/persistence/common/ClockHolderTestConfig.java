package com.ryuqq.setof.adapter.out.persistence.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Persistence Layer 통합 테스트용 인프라 빈 설정
 *
 * <p>Persistence Layer 통합 테스트에서 필요한 인프라 빈을 제공합니다.
 *
 * <p>운영 환경에서는 Bootstrap Layer에서 이러한 빈들이 제공되지만, Persistence Layer 단독 테스트에서는 이 설정이 필요한 빈들을 제공합니다.
 *
 * <p>제공하는 빈:
 *
 * <ul>
 *   <li>ClockHolder - 시간 추상화 인터페이스
 *   <li>ObjectMapper - JSON 직렬화/역직렬화
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 * @see com.ryuqq.setof.domain.common.util.ClockHolder
 */
@TestConfiguration
public class ClockHolderTestConfig {

    /**
     * 테스트용 ClockHolder Bean 등록
     *
     * <p>System Default Zone Clock을 사용하는 ClockHolder를 제공합니다.
     *
     * @return ClockHolder 인스턴스
     */
    @Bean
    public ClockHolder clockHolder() {
        Clock clock = Clock.system(ZoneId.systemDefault());
        return () -> clock;
    }

    /**
     * 테스트용 ObjectMapper Bean 등록
     *
     * <p>Java 8 날짜/시간 타입을 지원하는 ObjectMapper를 제공합니다.
     *
     * @return ObjectMapper 인스턴스
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}

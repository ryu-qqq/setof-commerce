package com.ryuqq.setof.adapter.in.rest.admin;

import com.ryuqq.setof.adapter.in.rest.admin.common.error.ErrorMapperRegistry;
import java.util.List;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 테스트용 Spring Boot Application 설정.
 *
 * <p>@WebMvcTest에서 사용하는 최소 설정입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@SpringBootApplication
public class TestConfiguration {

    /**
     * 테스트용 ErrorMapperRegistry 빈 제공.
     *
     * <p>GlobalExceptionHandler 의존성 해결을 위한 빈입니다.
     */
    @Bean
    @Primary
    public ErrorMapperRegistry errorMapperRegistry() {
        return new ErrorMapperRegistry(List.of());
    }
}

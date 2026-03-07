package com.ryuqq.setof.adapter.in.rest.common.config;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserIdResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 설정 - ArgumentResolver 등록.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticatedUserIdResolver authenticatedUserIdResolver;

    public WebMvcConfig(AuthenticatedUserIdResolver authenticatedUserIdResolver) {
        this.authenticatedUserIdResolver = authenticatedUserIdResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserIdResolver);
    }
}

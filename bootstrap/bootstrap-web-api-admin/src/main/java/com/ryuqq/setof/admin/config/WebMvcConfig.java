package com.ryuqq.setof.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration.
 *
 * <p>정적 리소스 핸들러 설정을 정의합니다.
 *
 * <p><strong>리소스 매핑:</strong>
 * <ul>
 *   <li>/v2/docs/** → classpath:/static/docs/ (REST Docs)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // REST Docs: /v2/docs/** → static/docs/
        registry.addResourceHandler("/v2/docs/**")
                .addResourceLocations("classpath:/static/docs/");
    }
}

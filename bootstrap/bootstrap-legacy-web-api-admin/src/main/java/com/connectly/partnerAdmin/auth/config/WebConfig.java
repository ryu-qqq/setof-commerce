package com.connectly.partnerAdmin.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.connectly.partnerAdmin.module.external.interceptor.BuymaWebhookInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final BuymaWebhookInterceptor buymaWebhookInterceptor;


    @Value(value = "${front.web-domain}")
    private String webDomain;

    @Value(value = "${front.admin-domain}")
    private String adminDomain;

    @Value(value = "${front.s3-domain}")
    private String s3Domain;

    @Value(value = "${front.s3-temp-domain}")
    private String s3TempDomain;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedOriginPatterns(webDomain, adminDomain, s3Domain, s3TempDomain, "https://www.set-of.com", "https://www.set-of.com")
                        .allowCredentials(true);
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(buymaWebhookInterceptor)
                .addPathPatterns("/api/v1/external/buyma/webhook");
    }

}
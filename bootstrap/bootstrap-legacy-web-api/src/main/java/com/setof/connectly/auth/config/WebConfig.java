package com.setof.connectly.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Value(value = "${front.web-domain}")
    private String webDomain;

    @Value(value = "${front.stage-domain}")
    private String stageDomain;

    @Value(value = "${front.s3-domain}")
    private String s3Domain;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedOrigins(webDomain, stageDomain, s3Domain)
                        .allowCredentials(true);
            }
        };
    }
}

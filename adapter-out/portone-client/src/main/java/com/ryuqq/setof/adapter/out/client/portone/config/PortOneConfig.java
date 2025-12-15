package com.ryuqq.setof.adapter.out.client.portone.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * PortOne Configuration
 *
 * <p>포트원(PortOne) API 클라이언트 설정을 구성합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(PortOneProperties.class)
public class PortOneConfig {

    /**
     * PortOne API용 RestClient 빈 생성
     *
     * @param properties PortOne 설정 값
     * @return RestClient 인스턴스
     */
    @Bean
    public RestClient portOneRestClient(PortOneProperties properties) {
        return RestClient.builder().baseUrl(properties.getApiUrl()).build();
    }
}

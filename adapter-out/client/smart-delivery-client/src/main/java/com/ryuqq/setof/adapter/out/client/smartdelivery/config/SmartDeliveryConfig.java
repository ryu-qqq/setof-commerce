package com.ryuqq.setof.adapter.out.client.smartdelivery.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * SmartDeliveryConfig - 스마트택배 API 클라이언트 설정
 *
 * <p>스마트택배(SweetTracker) API 클라이언트 설정을 구성합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(SmartDeliveryProperties.class)
public class SmartDeliveryConfig {

    /**
     * 스마트택배 API용 RestClient 빈 생성
     *
     * @param properties 스마트택배 설정 값
     * @return RestClient 인스턴스
     */
    @Bean
    public RestClient smartDeliveryRestClient(SmartDeliveryProperties properties) {
        return RestClient.builder().baseUrl(properties.getApiUrl()).build();
    }
}

package com.ryuqq.setof.adapter.out.nhn.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * NHN Cloud AlimTalk Client Configuration.
 *
 * <p>nhn.alimtalk.app-key 설정이 있을 때만 활성화됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Configuration
@EnableConfigurationProperties(NhnClientProperties.class)
@ConditionalOnProperty(prefix = "nhn.alimtalk", name = "app-key")
public class NhnClientConfig {

    @Bean
    public RestClient nhnAlimTalkRestClient(NhnClientProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl() + "/alimtalk/v2.3/appkeys/" + properties.appKey())
                .defaultHeader("X-Secret-Key", properties.secretKey())
                .defaultHeader("Content-Type", "application/json;charset=UTF-8")
                .build();
    }
}

package com.ryuqq.setof.adapter.out.nhn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * NHN Cloud 알림톡 클라이언트 Properties.
 *
 * @param appKey NHN Cloud App Key
 * @param secretKey NHN Cloud Secret Key
 * @param senderKey 발신 프로필 키
 * @param baseUrl NHN Cloud AlimTalk API Base URL
 * @author ryu-qqq
 * @since 1.1.0
 */
@ConfigurationProperties(prefix = "nhn.alimtalk")
public record NhnClientProperties(
        String appKey, String secretKey, String senderKey, String baseUrl) {

    public NhnClientProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://api-alimtalk.cloud.toast.com";
        }
    }
}

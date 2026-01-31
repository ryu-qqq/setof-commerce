package com.ryuqq.setof.adapter.out.client.smartdelivery.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SmartDeliveryProperties - 스마트택배 API 설정 값
 *
 * <p>스마트택배(SweetTracker) API 연동에 필요한 설정 값을 관리합니다.
 *
 * <p><strong>설정 예시 (application.yml):</strong>
 *
 * <pre>{@code
 * smart-delivery:
 *   api-url: https://info.sweettracker.co.kr
 *   api-key: your-api-key
 *   enabled: true
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "smart-delivery")
public class SmartDeliveryProperties {

    private String apiUrl = "https://info.sweettracker.co.kr";
    private String apiKey = "";
    private boolean enabled = false;

    public SmartDeliveryProperties() {}

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

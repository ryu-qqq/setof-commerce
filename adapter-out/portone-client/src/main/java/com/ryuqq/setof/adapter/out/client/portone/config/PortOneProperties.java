package com.ryuqq.setof.adapter.out.client.portone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * PortOne API Properties
 *
 * <p>포트원(PortOne) API 연동에 필요한 설정 값을 관리합니다.
 *
 * <p><strong>설정 예시 (application.yml):</strong>
 *
 * <pre>{@code
 * portone:
 *   api-url: https://api.iamport.kr
 *   api-key: your-api-key
 *   api-secret: your-api-secret
 *   enabled: true
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "portone")
public class PortOneProperties {

    private String apiUrl = "https://api.iamport.kr";
    private String apiKey = "";
    private String apiSecret = "";
    private boolean enabled = false;

    public PortOneProperties() {}

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

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

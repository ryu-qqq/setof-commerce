package com.setof.connectly.infra.config.fileflow;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Fileflow 서비스 연결 설정.
 *
 * <p>Service Discovery를 통해 Fileflow 서비스에 접근합니다.
 * 내부 서비스 간 통신에는 Service Token을 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "fileflow")
public class FileflowProperties {

    private String baseUrl;
    private String serviceToken;
    private int connectTimeoutMillis = 5000;
    private int readTimeoutMillis = 30000;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getServiceToken() {
        return serviceToken;
    }

    public void setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }
}

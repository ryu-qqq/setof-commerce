package com.ryuqq.setof.commerce.adapter.out.fileflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FileFlow Client 설정 Properties
 *
 * <p>FileFlow 서버 연동을 위한 설정 값들을 관리합니다.
 *
 * <p>application.yml 설정 예시:
 *
 * <pre>
 * fileflow:
 *   client:
 *     base-url: https://fileflow.example.com
 *     service-token: ${FILEFLOW_SERVICE_TOKEN}
 *     connect-timeout: 5000
 *     read-timeout: 30000
 * </pre>
 *
 * <p><strong>Zero-Tolerance 규칙 준수</strong>:
 *
 * <ul>
 *   <li>Lombok 금지
 *   <li>Plain Java Getter/Setter
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "fileflow.client")
public class FileflowClientProperties {

    private String baseUrl;
    private String serviceToken;
    private int connectTimeout = 5000;
    private int readTimeout = 30000;
    private String serviceName = "setof-commerce";

    public FileflowClientProperties() {}

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

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Service Token이 설정되어 있는지 확인합니다.
     *
     * @return Service Token이 설정되어 있으면 true
     */
    public boolean hasServiceToken() {
        return serviceToken != null && !serviceToken.isBlank();
    }
}

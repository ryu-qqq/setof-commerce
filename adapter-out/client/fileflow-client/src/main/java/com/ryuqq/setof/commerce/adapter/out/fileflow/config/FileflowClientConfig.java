package com.ryuqq.setof.commerce.adapter.out.fileflow.config;

import com.ryuqq.fileflow.sdk.client.FileFlowClient;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FileFlow Client 설정
 *
 * <p>FileFlow SDK를 사용한 클라이언트 Bean을 제공합니다.
 *
 * <p><strong>인증 방식</strong>:
 *
 * <ul>
 *   <li>ThreadLocal Token: 사용자 JWT 전파 (우선순위 1)
 *   <li>Service Token: 서버 간 통신용 (폴백)
 * </ul>
 *
 * <p><strong>Zero-Tolerance 규칙 준수</strong>:
 *
 * <ul>
 *   <li>Lombok 금지
 *   <li>생성자 주입
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(FileflowClientProperties.class)
public class FileflowClientConfig {

    private final FileflowClientProperties properties;

    public FileflowClientConfig(FileflowClientProperties properties) {
        this.properties = properties;
    }

    /**
     * FileFlow SDK 동기 클라이언트 Bean 생성
     *
     * <p>Service Token을 사용하여 서버 간 인증을 수행합니다. ThreadLocal에 토큰이 있으면 우선 사용됩니다.
     *
     * @return FileFlowClient instance
     */
    @Bean
    public FileFlowClient fileFlowClient() {
        return FileFlowClient.builder()
                .baseUrl(properties.getBaseUrl())
                .serviceToken(properties.getServiceToken())
                .connectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
                .readTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .build();
    }
}

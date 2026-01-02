package com.connectly.partnerAdmin.infra.config.fileflow;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Fileflow 클라이언트 설정.
 *
 * <p>Fileflow 서비스와 통신하기 위한 WebClient와 클라이언트 빈을 설정합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(FileflowProperties.class)
public class FileflowConfig {

    private static final String SERVICE_TOKEN_HEADER = "X-Service-Token";

    /**
     * Fileflow 전용 WebClient 생성.
     *
     * @param properties FileflowProperties
     * @return Fileflow 전용 WebClient
     */
    @Bean(name = "fileflowWebClient")
    public WebClient fileflowWebClient(FileflowProperties properties) {
        HttpClient httpClient =
                HttpClient.create()
                        .option(
                                ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                (int) properties.getConnectTimeoutMillis())
                        .responseTimeout(Duration.ofMillis(properties.getReadTimeoutMillis()));

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(SERVICE_TOKEN_HEADER, properties.getServiceToken())
                .build();
    }

    /**
     * FileflowClient 빈 생성.
     *
     * @param fileflowWebClient Fileflow 전용 WebClient
     * @return FileflowClient
     */
    @Bean
    public FileflowClient fileflowClient(WebClient fileflowWebClient) {
        return new FileflowClient(fileflowWebClient);
    }
}

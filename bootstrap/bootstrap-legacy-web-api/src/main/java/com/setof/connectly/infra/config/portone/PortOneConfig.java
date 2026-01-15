package com.setof.connectly.infra.config.portone;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PortOne (iamport) 클라이언트 설정
 * Stage 환경에서는 portone.enabled=false로 비활성화
 */
@Configuration
@ConditionalOnProperty(name = "portone.enabled", havingValue = "true", matchIfMissing = true)
public class PortOneConfig {

    @Value("${Import.import-restApi}")
    private String apiKey;

    @Value("${Import.import-restApiSecret}")
    private String apiSecret;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}

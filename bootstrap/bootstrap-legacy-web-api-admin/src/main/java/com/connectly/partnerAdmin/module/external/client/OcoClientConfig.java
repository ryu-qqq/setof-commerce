package com.connectly.partnerAdmin.module.external.client;

import com.connectly.partnerAdmin.module.order.enums.SiteName;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.connectly.partnerAdmin.module.external.aop.GetAccessTokenAop.accessTokenMap;

@Configuration
public class OcoClientConfig {

    @Value("${oco.api-key}")
    private String apiKey;


    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("ApiKey", apiKey);
            requestTemplate.header("token", accessTokenMap.getOrDefault(SiteName.OCO, ""));
        };
    }
}
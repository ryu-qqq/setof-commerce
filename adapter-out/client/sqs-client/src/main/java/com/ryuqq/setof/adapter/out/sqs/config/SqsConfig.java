package com.ryuqq.setof.adapter.out.sqs.config;

import java.net.URI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * AWS SQS Client Configuration.
 *
 * <p>SqsClient 빈을 생성합니다.
 *
 * <p>sqs.region 설정이 있을 때 활성화됩니다. 개별 어댑터는 각자의 ConditionalOnProperty로 큐별 활성화를 제어합니다.
 */
@Configuration
@EnableConfigurationProperties(SqsClientProperties.class)
@ConditionalOnProperty(prefix = "sqs", name = "region")
public class SqsConfig {

    @Bean
    public SqsClient sqsClient(SqsClientProperties properties) {
        var builder = SqsClient.builder().region(Region.of(properties.getRegion()));

        if (properties.getEndpoint() != null && !properties.getEndpoint().isBlank()) {
            builder.endpointOverride(URI.create(properties.getEndpoint()))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create("test", "test")));
        }

        return builder.build();
    }
}

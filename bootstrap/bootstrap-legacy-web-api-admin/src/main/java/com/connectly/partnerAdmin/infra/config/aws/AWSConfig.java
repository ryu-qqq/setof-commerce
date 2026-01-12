package com.connectly.partnerAdmin.infra.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * AWS 클라이언트 설정.
 *
 * <p>LambdaClient와 SqsClient는 이미지 업로드 처리에 사용됩니다.
 * Presigned URL 발급은 Fileflow 서비스를 통해 처리됩니다.
 */
@Configuration
public class AWSConfig {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public LambdaClient lambdaClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider staticCredentialsProvider =
                StaticCredentialsProvider.create(awsCredentials);

        return LambdaClient.builder()
                .region(Region.of(region))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public SqsClient sqsClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider staticCredentialsProvider =
                StaticCredentialsProvider.create(awsCredentials);

        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider staticCredentialsProvider =
                StaticCredentialsProvider.create(awsCredentials);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider staticCredentialsProvider =
                StaticCredentialsProvider.create(awsCredentials);

        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }
}

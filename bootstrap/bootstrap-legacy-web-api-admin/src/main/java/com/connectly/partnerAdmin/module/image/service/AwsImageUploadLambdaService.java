package com.connectly.partnerAdmin.module.image.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.module.image.dto.FileUploadRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@Service
public class AwsImageUploadLambdaService {

    private static final String IMAGE_UPLOAD_LAMBDA_SERVICE = "ImageUploadTrigger";
    private static final Logger logger = LoggerFactory.getLogger(AwsImageUploadLambdaService.class);

    private final ExecutorService virtualThreadExecutor;
    private final LambdaClient lambdaClient;
    private final ObjectMapper objectMapper;

    public AwsImageUploadLambdaService(ExecutorService virtualThreadExecutor, LambdaClient lambdaClient, ObjectMapper objectMapper) {
        this.virtualThreadExecutor = virtualThreadExecutor;
        this.lambdaClient = lambdaClient;
        this.objectMapper = objectMapper;
    }

    public void triggerAwsLambda(FileUploadRequestDto fileUploadRequestDto) {
        try {
            String payload = objectMapper.writeValueAsString(fileUploadRequestDto);

            InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName(IMAGE_UPLOAD_LAMBDA_SERVICE)
                .payload(SdkBytes.fromUtf8String(payload))
                .build();

            CompletableFuture.supplyAsync(() -> lambdaClient.invoke(invokeRequest), virtualThreadExecutor)
                .thenAccept(this::logLambdaResponse)
                .exceptionally(ex -> {
                    logger.error("An error occurred while invoking AWS Lambda", ex);
                    return null;
                });

        } catch (Exception e) {
            logger.error("An error occurred during file upload", e);
        }
    }

    private void logLambdaResponse(InvokeResponse response) {
        logger.info("AWS Lambda response received: Status Code={}, Payload={}",
            response.statusCode(),
            response.payload().asUtf8String());
    }

}

package com.connectly.partnerAdmin.module.image.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.module.image.dto.FileUploadRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SqsMessageSender {

	private final SqsClient sqsClient;
	private final ExecutorService executorService;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(SqsMessageSender.class);

	private static final String QUEUE_URL = "https://sqs.ap-northeast-2.amazonaws.com/646886795421/image-upload-done-queue";


	public SqsMessageSender(SqsClient sqsClient, ExecutorService executorService, ObjectMapper objectMapper) {
		this.sqsClient = sqsClient;
		this.executorService = executorService;
		this.objectMapper = objectMapper;
	}

	public void sendMessage(FileUploadRequestDto fileUploadRequestDto) {
		logger.info("[SQS] Image upload message preparing - imageUrl: {}, originUrl: {}, contentType: {}",
				fileUploadRequestDto.imageUrl(),
				fileUploadRequestDto.originUrl(),
				fileUploadRequestDto.contentType());

		try {
			String payload = objectMapper.writeValueAsString(fileUploadRequestDto);
			logger.info("[SQS] Message payload created - payload: {}", payload);

			SendMessageRequest request = SendMessageRequest.builder()
				.queueUrl(QUEUE_URL)
				.messageBody(payload)
				.build();

			CompletableFuture.runAsync(() -> {
				try {
					logger.info("[SQS] Sending message to queue: {}", QUEUE_URL);
					SendMessageResponse response = sqsClient.sendMessage(request);
					logger.info("[SQS] Message sent successfully - messageId: {}, sequenceNumber: {}",
							response.messageId(),
							response.sequenceNumber());
				} catch (Exception ex) {
					logger.error("[SQS] Message sending failed - queueUrl: {}, imageUrl: {}, originUrl: {}",
							QUEUE_URL,
							fileUploadRequestDto.imageUrl(),
							fileUploadRequestDto.originUrl(),
							ex);
				}
			}, executorService);

		} catch (Exception e) {
			logger.error("[SQS] Payload serialization failed - imageUrl: {}, originUrl: {}",
					fileUploadRequestDto.imageUrl(),
					fileUploadRequestDto.originUrl(),
					e);
		}
	}
}

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

@Service
public class SqsMessageSender {

	private final SqsClient sqsClient;
	private final ExecutorService executorService;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(SqsMessageSender.class);

	private static final String queueUrl =  "https://sqs.ap-northeast-2.amazonaws.com/646886795421/image-upload-done-queue";


	public SqsMessageSender(SqsClient sqsClient, ExecutorService executorService, ObjectMapper objectMapper) {
		this.sqsClient = sqsClient;
		this.executorService = executorService;
		this.objectMapper = objectMapper;
	}

	public void sendMessage(FileUploadRequestDto fileUploadRequestDto) {

		try {
			String payload = objectMapper.writeValueAsString(fileUploadRequestDto);

			SendMessageRequest request = SendMessageRequest.builder()
				.queueUrl(queueUrl)
				.messageBody(payload)
				.build();

			CompletableFuture.runAsync(() -> {
				try {
					sqsClient.sendMessage(request);
					logger.info("SQS Message sent successfully");
				} catch (Exception ex) {
					logger.error("SQS Message sending failed", ex);
				}
			}, executorService);

		} catch (Exception e) {
			logger.error("File Upload SQS Error Occured. DTO: {}", fileUploadRequestDto, e);
		}




	}

}

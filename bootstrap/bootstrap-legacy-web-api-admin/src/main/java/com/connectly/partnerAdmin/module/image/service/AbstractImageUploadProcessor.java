package com.connectly.partnerAdmin.module.image.service;

import org.springframework.beans.factory.annotation.Value;

import com.connectly.partnerAdmin.module.image.dto.FileUploadRequestDto;

public abstract class AbstractImageUploadProcessor implements ImageUploadProcessor {

    @Value("${aws.assetUrl}")
    protected String assetUrl;

    private final SqsMessageSender sqsMessageSender;

    protected AbstractImageUploadProcessor(SqsMessageSender sqsMessageSender) {
        this.sqsMessageSender = sqsMessageSender;
    }

    public void trigger(String imageUrl, String originUrl, String contentType){
        FileUploadRequestDto fileUploadRequestDto = new FileUploadRequestDto(imageUrl, originUrl, contentType);
        sqsMessageSender.sendMessage(fileUploadRequestDto);
    }

}

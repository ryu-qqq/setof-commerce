package com.connectly.partnerAdmin.module.image.service;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.image.enums.PathType;


@Component
public class DefaultImageUploadProcessor extends AbstractImageUploadProcessor {

    protected DefaultImageUploadProcessor(SqsMessageSender sqsMessageSender) {
        super(sqsMessageSender);
    }

    @Override
    public String uploadImage(Long id, String imageUrl, PathType pathType) {
        return processImageUpload(id, imageUrl, pathType);
    }


    public String processImageUpload(Long id, String originUrl, PathType pathType) {
        String fileExtension = ImageFileTypeUtils.getFileExtension(originUrl);
        String imageUrl = ImageFileNameGenerator.generate(id, assetUrl, pathType, fileExtension);
        trigger(imageUrl, originUrl, ImageFileTypeUtils.getImageContentType(imageUrl));
        return imageUrl;
    }


}
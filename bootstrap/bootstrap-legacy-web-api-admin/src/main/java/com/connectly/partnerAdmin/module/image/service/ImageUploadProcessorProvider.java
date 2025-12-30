package com.connectly.partnerAdmin.module.image.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.image.enums.PathType;

@Component
public class ImageUploadProcessorProvider {


    private final List<ImageUploadProcessor> imageUploadProcessors;

    public ImageUploadProcessorProvider(List<ImageUploadProcessor> imageUploadProcessors) {
        this.imageUploadProcessors = imageUploadProcessors;
    }

    public ImageUploadProcessor findProcessor(PathType pathType) {
        return imageUploadProcessors.stream()
            .filter(processor ->
                (pathType == PathType.DESCRIPTION && processor instanceof HtmlImageUploadProcessor) ||
                    (pathType != PathType.DESCRIPTION && processor instanceof DefaultImageUploadProcessor)
            )
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No ImageUploadProcessor found : " +  pathType));
    }



}

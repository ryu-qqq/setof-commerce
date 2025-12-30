package com.connectly.partnerAdmin.module.image.service;

import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.module.image.core.ImageContext;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.enums.PathType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService implements ImageUploadService{

    private final ImageUploadProcessorProvider imageUploadProcessorProvider;

    @Override
    public String uploadImage(ImageContext imageContext) {
        ImagePath imagePath = imageContext.getImagePath();
        PathType pathType = PathType.valueOf(imagePath.getName());

        ImageUploadProcessor processor = imageUploadProcessorProvider.findProcessor(pathType);
        return processor.uploadImage(null, imageContext.getImageUrl(), pathType);
    }


}

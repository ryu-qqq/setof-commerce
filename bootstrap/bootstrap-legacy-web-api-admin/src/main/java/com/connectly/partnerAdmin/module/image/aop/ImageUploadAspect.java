package com.connectly.partnerAdmin.module.image.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.image.core.BaseImageContext;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.service.ImageUploadService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class ImageUploadAspect {

    protected final static String MUSTIT = "mustit";


    @Value("${aws.assetUrl}")
    protected String assetUrl;

    @Value("${aws.tempUrl}")
    protected String tempUrl;

    private final ImageUploadService imageUploadService;

    protected String uploadImage(BaseImageContext baseImageContext){
        if(baseImageContext.getImageUrl().contains(assetUrl)) {
            return baseImageContext.getImageUrl();
        }
        return triggerFileInAwsLamda(baseImageContext);
    }


    protected String uploadImage(String description) {
        BaseImageContext baseImageContext1 = new BaseImageContext(ImagePath.DESCRIPTION, description);
        return triggerFileInAwsLamda(baseImageContext1);
    }

    protected String triggerFileInAwsLamda(BaseImageContext baseImageContext) {
        return imageUploadService.uploadImage(baseImageContext);
    }


}

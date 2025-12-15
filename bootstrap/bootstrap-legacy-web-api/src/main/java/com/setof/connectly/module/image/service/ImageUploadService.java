package com.setof.connectly.module.image.service;

import com.setof.connectly.module.image.dto.PreSignedUrlRequest;
import com.setof.connectly.module.image.dto.PreSignedUrlResponse;
import com.setof.connectly.module.image.enums.ImagePath;
import java.util.concurrent.CompletableFuture;

public interface ImageUploadService {

    PreSignedUrlResponse getPreSignedUrl(PreSignedUrlRequest preSignedUrlRequest);

    CompletableFuture<String> uploadImage(ImagePath imagePath, String originalImageUrl);
}

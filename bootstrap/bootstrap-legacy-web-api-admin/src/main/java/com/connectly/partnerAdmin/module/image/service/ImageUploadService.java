package com.connectly.partnerAdmin.module.image.service;

import com.connectly.partnerAdmin.module.image.core.ImageContext;
import com.connectly.partnerAdmin.module.image.dto.PreSignedUrlRequest;
import com.connectly.partnerAdmin.module.image.dto.PreSignedUrlResponse;

public interface ImageUploadService {

    String uploadImage(ImageContext imageContext);

    PreSignedUrlResponse getPreSignedUrl(PreSignedUrlRequest request);

    void completeUpload(String sessionId, String etag);

}


package com.setof.connectly.module.image.service;

import com.setof.connectly.module.image.dto.PreSignedUrlRequest;
import com.setof.connectly.module.image.dto.PreSignedUrlResponse;
import com.setof.connectly.module.image.enums.ImagePath;
import java.util.concurrent.CompletableFuture;

public interface ImageUploadService {

    PreSignedUrlResponse getPreSignedUrl(PreSignedUrlRequest preSignedUrlRequest);

    CompletableFuture<String> uploadImage(ImagePath imagePath, String originalImageUrl);

    /**
     * 업로드 완료 처리.
     *
     * <p>클라이언트가 Presigned URL로 S3에 업로드 완료 후 호출합니다.
     *
     * @param sessionId FileFlow 세션 ID
     * @param etag S3 업로드 후 반환된 ETag
     */
    void completeUpload(String sessionId, String etag);
}

package com.setof.connectly.module.image.service;

import com.setof.connectly.module.image.dto.PreSignedUrlRequest;
import com.setof.connectly.module.image.dto.PreSignedUrlResponse;
import com.setof.connectly.module.image.enums.ImagePath;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * FileFlow 마이그레이션 중 임시 Stub 구현체.
 *
 * <p>외부 URL 이미지는 그대로 반환하고, presigned 기능은 비활성화됨.
 */
@Slf4j
@Service
public class StubImageUploadService implements ImageUploadService {

    private static final String DEFAULT_IMAGE_URL =
            "https://d3fej89xf1vai5.cloudfront.net/logo/setof-logo.png";

    @Value("${aws.assetUrl}")
    private String assetUrl;

    @Override
    public PreSignedUrlResponse getPreSignedUrl(PreSignedUrlRequest preSignedUrlRequest) {
        log.warn("FileFlow 마이그레이션 진행 중 - presigned URL 기능 비활성화");
        throw new UnsupportedOperationException("FileFlow 마이그레이션 진행 중 - 임시 비활성화");
    }

    @Override
    public CompletableFuture<String> uploadImage(ImagePath imagePath, String originalImageUrl) {
        // 이미 CDN에 있는 이미지는 그대로 반환
        if (originalImageUrl != null && originalImageUrl.contains(assetUrl)) {
            return CompletableFuture.completedFuture(originalImageUrl);
        }
        // 외부 URL은 기본 이미지로 대체 (FileFlow 없이는 업로드 불가)
        log.warn("FileFlow 마이그레이션 진행 중 - 외부 URL 업로드 비활성화: {}", originalImageUrl);
        return CompletableFuture.completedFuture(
                originalImageUrl != null ? originalImageUrl : DEFAULT_IMAGE_URL);
    }

    @Override
    public void completeUpload(String sessionId, String etag) {
        log.warn("FileFlow 마이그레이션 진행 중 - 업로드 완료 기능 비활성화");
        throw new UnsupportedOperationException("FileFlow 마이그레이션 진행 중 - 임시 비활성화");
    }
}

package com.setof.connectly.module.image.service;

import com.ryuqq.fileflow.sdk.client.FileFlowClient;
import com.ryuqq.fileflow.sdk.exception.FileFlowException;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadRequest;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadResponse;
import com.setof.connectly.module.image.dto.PreSignedUrlRequest;
import com.setof.connectly.module.image.dto.PreSignedUrlResponse;
import com.setof.connectly.module.image.enums.ImagePath;
import com.setof.connectly.module.utils.FileUtils;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * FileFlow SDK 기반 이미지 업로드 서비스.
 *
 * <p>FileFlow 서비스를 통해 S3 Presigned URL을 발급받고,
 * 외부 URL 이미지를 CDN으로 업로드합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Slf4j
@Service
public class S3UploadService implements ImageUploadService {

    private static final long DEFAULT_FILE_SIZE = 10 * 1024 * 1024L; // 10MB
    private static final String DEFAULT_IMAGE_URL =
            "https://d3fej89xf1vai5.cloudfront.net/logo/setof-logo.png";

    private final FileFlowClient fileFlowClient;
    private final RestClient fileflowRestClient;

    @Value("${aws.assetUrl}")
    private String assetUrl;

    public S3UploadService(FileFlowClient fileFlowClient, RestClient fileflowRestClient) {
        this.fileFlowClient = fileFlowClient;
        this.fileflowRestClient = fileflowRestClient;
    }

    /**
     * Presigned URL 발급.
     *
     * @param preSignedUrlRequest 요청 정보 (파일명, 이미지 경로)
     * @return PreSignedUrlResponse (sessionId, presignedUrl, objectKey)
     */
    @Override
    public PreSignedUrlResponse getPreSignedUrl(PreSignedUrlRequest preSignedUrlRequest) {
        String fileName = preSignedUrlRequest.getFileName();
        ImagePath imagePath = preSignedUrlRequest.getImagePath();
        String contentType = FileUtils.getContentType(imagePath);
        String category = toUploadCategory(imagePath);

        log.debug("FileFlow presigned URL 요청: fileName={}, category={}", fileName, category);

        try {
            InitSingleUploadRequest sdkRequest = InitSingleUploadRequest.builder()
                    .filename(fileName)
                    .contentType(contentType)
                    .fileSize(DEFAULT_FILE_SIZE)
                    .category(category)
                    .build();

            InitSingleUploadResponse response = fileFlowClient.uploadSessions().initSingle(sdkRequest);

            log.info("FileFlow presigned URL 발급 성공: sessionId={}", response.getSessionId());

            return PreSignedUrlResponse.builder()
                    .sessionId(response.getSessionId())
                    .preSignedUrl(response.getPresignedUrl())
                    .objectKey(response.getS3Key())
                    .build();

        } catch (FileFlowException e) {
            log.error("FileFlow presigned URL 발급 실패: {}", e.getMessage(), e);
            throw new RuntimeException("Presigned URL 발급 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 외부 URL 이미지를 CDN으로 업로드.
     *
     * <p>FileFlow의 externalDownloads API를 사용하여
     * 외부 URL에서 이미지를 다운로드하고 CDN에 업로드합니다.
     *
     * @param imagePath 이미지 카테고리
     * @param originalImageUrl 원본 이미지 URL
     * @return 새 CDN URL
     */
    @Async
    @Override
    public CompletableFuture<String> uploadImage(ImagePath imagePath, String originalImageUrl) {
        // 이미 CDN에 있는 이미지는 그대로 반환
        if (originalImageUrl.contains(assetUrl)) {
            return CompletableFuture.completedFuture(originalImageUrl);
        }

        String category = toUploadCategory(imagePath);
        String filename = extractFilename(originalImageUrl);

        log.debug("FileFlow 외부 URL 업로드 요청: sourceUrl={}, category={}", originalImageUrl, category);

        try {
            String newCdnUrl = fileFlowClient.externalDownloads()
                    .request(originalImageUrl, category, filename);

            log.info("FileFlow 외부 URL 업로드 성공: sourceUrl={}, newCdnUrl={}",
                    originalImageUrl, newCdnUrl);

            return CompletableFuture.completedFuture(newCdnUrl);

        } catch (FileFlowException e) {
            log.error("FileFlow 외부 URL 업로드 실패: sourceUrl={}, error={}",
                    originalImageUrl, e.getMessage(), e);
            return CompletableFuture.completedFuture(DEFAULT_IMAGE_URL);
        } catch (Exception e) {
            log.error("FileFlow 외부 URL 업로드 중 예외: sourceUrl={}, error={}",
                    originalImageUrl, e.getMessage(), e);
            return CompletableFuture.completedFuture(DEFAULT_IMAGE_URL);
        }
    }

    /**
     * 업로드 완료 처리.
     *
     * <p>SDK에서 지원하지 않는 API이므로 RestClient를 사용하여 직접 호출합니다.
     *
     * @param sessionId FileFlow 세션 ID
     * @param etag S3 업로드 후 반환된 ETag
     */
    @Override
    public void completeUpload(String sessionId, String etag) {
        log.debug("FileFlow 업로드 완료 처리: sessionId={}", sessionId);

        try {
            fileflowRestClient
                    .put()
                    .uri("/api/v1/upload-sessions/{sessionId}/complete", sessionId)
                    .body(Map.of("etag", etag))
                    .retrieve()
                    .toBodilessEntity();

            log.info("FileFlow 업로드 완료 성공: sessionId={}", sessionId);

        } catch (Exception e) {
            log.error("FileFlow 업로드 완료 실패: sessionId={}, error={}", sessionId, e.getMessage(), e);
            throw new RuntimeException("업로드 완료 처리 실패: " + e.getMessage(), e);
        }
    }

    /**
     * ImagePath를 FileFlow uploadCategory로 변환.
     */
    private String toUploadCategory(ImagePath imagePath) {
        return imagePath.name();
    }

    /**
     * URL에서 파일명 추출.
     */
    private String extractFilename(String url) {
        try {
            String path = new java.net.URL(url).getPath();
            int lastSlash = path.lastIndexOf('/');
            return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
        } catch (Exception e) {
            return "image.jpg";
        }
    }
}

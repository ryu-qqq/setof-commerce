package com.connectly.partnerAdmin.module.coreServer;

import com.connectly.partnerAdmin.module.coreServer.response.PreSignedUrlResponseDto;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.ryuqq.fileflow.sdk.client.FileFlowClient;
import com.ryuqq.fileflow.sdk.exception.FileFlowException;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadRequest;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Presigned URL 생성 서비스.
 *
 * <p>FileFlow SDK를 통해 S3 Presigned URL을 발급받습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class PreSignedUrlGenerateService {

    private static final Logger log = LoggerFactory.getLogger(PreSignedUrlGenerateService.class);

    private final FileFlowClient fileFlowClient;
    private final RestClient fileflowRestClient;

    public PreSignedUrlGenerateService(FileFlowClient fileFlowClient, RestClient fileflowRestClient) {
        this.fileFlowClient = fileFlowClient;
        this.fileflowRestClient = fileflowRestClient;
    }

    /**
     * Presigned URL 발급.
     *
     * @param fileName 파일명 (확장자 포함)
     * @param imagePath 이미지 경로 (카테고리)
     * @return PreSignedUrlResponseDto
     */
    public PreSignedUrlResponseDto getPreSignedUrl(String fileName, ImagePath imagePath) {
        return getPreSignedUrl(fileName, imagePath, 10 * 1024 * 1024L); // 10MB default
    }

    /**
     * Presigned URL 발급 (파일 크기 지정).
     *
     * @param fileName 파일명 (확장자 포함)
     * @param imagePath 이미지 경로 (카테고리)
     * @param fileSize 파일 크기 (bytes)
     * @return PreSignedUrlResponseDto
     */
    public PreSignedUrlResponseDto getPreSignedUrl(String fileName, ImagePath imagePath, long fileSize) {
        String contentType = ImageFileTypeUtils.getImageContentType(fileName);
        String category = toUploadCategory(imagePath);

        log.debug("FileFlow presigned URL 요청: fileName={}, category={}", fileName, category);

        try {
            InitSingleUploadRequest sdkRequest = InitSingleUploadRequest.builder()
                    .filename(fileName)
                    .contentType(contentType)
                    .fileSize(fileSize)
                    .category(category)
                    .build();

            InitSingleUploadResponse response = fileFlowClient.uploadSessions().initSingle(sdkRequest);

            log.info("FileFlow presigned URL 발급 성공: sessionId={}", response.getSessionId());

            return new PreSignedUrlResponseDto(
                    response.getSessionId(),
                    response.getPresignedUrl(),
                    response.getS3Key()
            );

        } catch (FileFlowException e) {
            log.error("FileFlow presigned URL 발급 실패: {}", e.getMessage(), e);
            throw new RuntimeException("Presigned URL 발급 실패: " + e.getMessage(), e);
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
}

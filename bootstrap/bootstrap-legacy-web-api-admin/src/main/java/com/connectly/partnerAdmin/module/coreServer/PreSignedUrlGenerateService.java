package com.connectly.partnerAdmin.module.coreServer;

import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.infra.config.fileflow.FileflowClient;
import com.connectly.partnerAdmin.infra.config.fileflow.dto.InitSingleUploadResponse;
import com.connectly.partnerAdmin.module.coreServer.response.PreSignedUrlResponseDto;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;

/**
 * Presigned URL 생성 서비스.
 *
 * <p>Fileflow 서비스를 통해 S3 Presigned URL을 발급받습니다.
 */
@Service
public class PreSignedUrlGenerateService {

    private final FileflowClient fileflowClient;

    public PreSignedUrlGenerateService(FileflowClient fileflowClient) {
        this.fileflowClient = fileflowClient;
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
        String uploadCategory = toUploadCategory(imagePath);

        InitSingleUploadResponse response = fileflowClient.initSingleUpload(
            fileName,
            fileSize,
            contentType,
            uploadCategory
        );

        return new PreSignedUrlResponseDto(
            response.sessionId(),
            response.presignedUrl(),
            response.key()
        );
    }

    /**
     * 업로드 완료 처리.
     *
     * @param sessionId Fileflow 세션 ID
     * @param etag S3 업로드 후 반환된 ETag
     */
    public void completeUpload(String sessionId, String etag) {
        fileflowClient.completeSingleUpload(sessionId, etag);
    }

    /**
     * ImagePath를 Fileflow uploadCategory로 변환.
     */
    private String toUploadCategory(ImagePath imagePath) {
        return switch (imagePath) {
            case PRODUCT -> "PRODUCT";
            case DESCRIPTION -> "DESCRIPTION";
            case QNA -> "QNA";
            case CONTENT -> "CONTENT";
            case IMAGE_COMPONENT -> "IMAGE_COMPONENT";
            case BANNER -> "BANNER";
        };
    }
}

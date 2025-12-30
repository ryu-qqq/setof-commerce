package com.connectly.partnerAdmin.module.coreServer.response;

/**
 * Presigned URL 응답 DTO.
 *
 * @param sessionId Fileflow 세션 ID (업로드 완료 처리에 필요)
 * @param preSignedUrl S3 Presigned URL
 * @param objectKey S3 객체 키
 */
public record PreSignedUrlResponseDto(
    String sessionId,
    String preSignedUrl,
    String objectKey
) {
    /**
     * 하위 호환성을 위한 생성자 (sessionId 없이).
     */
    public PreSignedUrlResponseDto(String preSignedUrl, String objectKey) {
        this(null, preSignedUrl, objectKey);
    }
}

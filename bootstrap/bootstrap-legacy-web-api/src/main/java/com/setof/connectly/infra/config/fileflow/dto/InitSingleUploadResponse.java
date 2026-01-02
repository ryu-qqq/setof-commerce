package com.setof.connectly.infra.config.fileflow.dto;

import java.time.Instant;

/**
 * Fileflow 단일 업로드 세션 초기화 응답.
 *
 * @param sessionId 세션 ID
 * @param presignedUrl Presigned URL (15분 유효)
 * @param expiresAt 세션 만료 시각 (UTC)
 * @param bucket S3 버킷명
 * @param key S3 객체 키
 * @author development-team
 * @since 1.0.0
 */
public record InitSingleUploadResponse(
        String sessionId,
        String presignedUrl,
        Instant expiresAt,
        String bucket,
        String key) {

    /**
     * 응답에서 파일 URL 생성.
     *
     * @param assetUrl CloudFront 또는 S3 URL prefix
     * @return 완성된 파일 접근 URL
     */
    public String toFileUrl(String assetUrl) {
        return "https://" + assetUrl + "/" + key;
    }
}

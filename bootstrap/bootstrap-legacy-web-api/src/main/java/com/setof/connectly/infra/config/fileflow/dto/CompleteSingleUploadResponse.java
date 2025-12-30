package com.setof.connectly.infra.config.fileflow.dto;

import java.time.Instant;

/**
 * Fileflow 단일 업로드 완료 응답.
 *
 * @param sessionId 세션 ID
 * @param status 세션 상태 (COMPLETED)
 * @param bucket S3 버킷명
 * @param key S3 객체 키
 * @param etag S3 ETag
 * @param completedAt 완료 시각 (UTC)
 * @author development-team
 * @since 1.0.0
 */
public record CompleteSingleUploadResponse(
        String sessionId,
        String status,
        String bucket,
        String key,
        String etag,
        Instant completedAt) {

    /**
     * 업로드 완료 여부 확인.
     *
     * @return 완료 상태인 경우 true
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

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

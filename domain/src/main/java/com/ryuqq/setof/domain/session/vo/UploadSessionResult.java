package com.ryuqq.setof.domain.session.vo;

import java.time.Instant;

/**
 * 업로드 세션 생성 결과 VO.
 *
 * <p>Presigned URL 발급 결과를 담습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class UploadSessionResult {

    private final String sessionId;
    private final String presignedUrl;
    private final String objectKey;
    private final Instant expiresAt;
    private final String accessUrl;

    public UploadSessionResult(
            String sessionId,
            String presignedUrl,
            String objectKey,
            Instant expiresAt,
            String accessUrl) {
        this.sessionId = sessionId;
        this.presignedUrl = presignedUrl;
        this.objectKey = objectKey;
        this.expiresAt = expiresAt;
        this.accessUrl = accessUrl;
    }

    public String sessionId() {
        return sessionId;
    }

    public String presignedUrl() {
        return presignedUrl;
    }

    public String objectKey() {
        return objectKey;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public String accessUrl() {
        return accessUrl;
    }
}

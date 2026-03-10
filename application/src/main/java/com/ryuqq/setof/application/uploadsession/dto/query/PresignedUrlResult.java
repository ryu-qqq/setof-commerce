package com.ryuqq.setof.application.uploadsession.dto.query;

import java.time.Instant;

/**
 * Presigned URL 발급 결과.
 *
 * @param sessionId 업로드 세션 ID
 * @param presignedUrl 프리사인드 URL
 * @param objectKey S3 Object Key
 * @param expiresAt URL 만료 시간
 * @param accessUrl CDN 접근 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PresignedUrlResult(
        String sessionId,
        String presignedUrl,
        String objectKey,
        Instant expiresAt,
        String accessUrl) {}

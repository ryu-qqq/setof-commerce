package com.ryuqq.setof.application.image.dto;

import java.time.Instant;

/**
 * Presigned URL 발급 결과 DTO
 *
 * @param uploadUrl 클라이언트가 PUT 요청을 보낼 URL
 * @param accessUrl 업로드 완료 후 접근 가능한 URL
 * @param fileName 저장된 파일명
 * @param expiresAt URL 만료 시간
 * @author development-team
 * @since 2.0.0
 */
public record PreSignedUrlResult(
        String uploadUrl, String accessUrl, String fileName, Instant expiresAt) {

    /**
     * 정적 팩토리 메서드
     *
     * @param uploadUrl 업로드 URL
     * @param accessUrl 접근 URL
     * @param fileName 파일명
     * @param expiresAt 만료 시간
     * @return PreSignedUrlResult 인스턴스
     */
    public static PreSignedUrlResult of(
            String uploadUrl, String accessUrl, String fileName, Instant expiresAt) {
        return new PreSignedUrlResult(uploadUrl, accessUrl, fileName, expiresAt);
    }

    /**
     * URL이 만료되었는지 확인
     *
     * @return 만료되었으면 true
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}

package com.connectly.partnerAdmin.infra.config.fileflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Fileflow 단일 업로드 세션 초기화 요청.
 *
 * @param idempotencyKey 멱등성 키 (UUID)
 * @param fileName 파일명 (확장자 포함)
 * @param fileSize 파일 크기 (bytes)
 * @param contentType Content-Type (MIME 타입)
 * @param userId 사용자 ID (Customer 전용, null 가능)
 * @param userEmail 사용자 이메일 (Admin/Seller 전용, null 가능)
 * @param uploadCategory 업로드 카테고리
 * @param customPath 커스텀 S3 경로 (SYSTEM 전용)
 * @author development-team
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InitSingleUploadRequest(
        String idempotencyKey,
        String fileName,
        long fileSize,
        String contentType,
        Long userId,
        String userEmail,
        String uploadCategory,
        String customPath) {

    /**
     * 빌더 패턴을 위한 생성자.
     *
     * @param builder InitSingleUploadRequestBuilder
     */
    private InitSingleUploadRequest(Builder builder) {
        this(
                builder.idempotencyKey,
                builder.fileName,
                builder.fileSize,
                builder.contentType,
                builder.userId,
                builder.userEmail,
                builder.uploadCategory,
                builder.customPath);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String idempotencyKey;
        private String fileName;
        private long fileSize;
        private String contentType;
        private Long userId;
        private String userEmail;
        private String uploadCategory;
        private String customPath;

        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder fileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder uploadCategory(String uploadCategory) {
            this.uploadCategory = uploadCategory;
            return this;
        }

        public Builder customPath(String customPath) {
            this.customPath = customPath;
            return this;
        }

        public InitSingleUploadRequest build() {
            return new InitSingleUploadRequest(this);
        }
    }
}

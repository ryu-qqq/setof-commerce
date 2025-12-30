package com.connectly.partnerAdmin.infra.config.fileflow.dto;

/**
 * Fileflow 단일 업로드 완료 요청.
 *
 * @param etag S3가 반환한 ETag
 * @author development-team
 * @since 1.0.0
 */
public record CompleteSingleUploadRequest(String etag) {

    /**
     * ETag로 요청 생성.
     *
     * @param etag S3 업로드 후 반환된 ETag
     * @return CompleteSingleUploadRequest
     */
    public static CompleteSingleUploadRequest of(String etag) {
        return new CompleteSingleUploadRequest(etag);
    }
}

package com.ryuqq.setof.application.uploadsession.dto.command;

/**
 * 업로드 세션 완료 커맨드.
 *
 * @param sessionId FileFlow 세션 ID
 * @param fileSize 파일 크기 (바이트)
 * @param etag S3 업로드 후 반환된 ETag
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CompleteUploadSessionCommand(String sessionId, long fileSize, String etag) {

    public static CompleteUploadSessionCommand of(String sessionId, String etag) {
        return new CompleteUploadSessionCommand(sessionId, 0L, etag);
    }
}

package com.ryuqq.setof.domain.session.vo;

/**
 * 업로드 세션 완료 VO.
 *
 * <p>S3에 업로드 완료 후 세션 완료 처리에 필요한 정보를 담습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class CompleteUploadSession {

    private static final long DEFAULT_FILE_SIZE = 10L * 1024 * 1024;

    private final String sessionId;
    private final long fileSize;
    private final String etag;

    private CompleteUploadSession(String sessionId, long fileSize, String etag) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("세션 ID는 필수입니다");
        }
        if (etag == null || etag.isBlank()) {
            throw new IllegalArgumentException("ETag는 필수입니다");
        }
        this.sessionId = sessionId;
        this.fileSize = fileSize;
        this.etag = etag;
    }

    public static CompleteUploadSession of(String sessionId, long fileSize, String etag) {
        return new CompleteUploadSession(sessionId, fileSize, etag);
    }

    public static CompleteUploadSession withDefaultSize(String sessionId, String etag) {
        return new CompleteUploadSession(sessionId, DEFAULT_FILE_SIZE, etag);
    }

    public String sessionId() {
        return sessionId;
    }

    public long fileSize() {
        return fileSize;
    }

    public String etag() {
        return etag;
    }
}

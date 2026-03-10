package com.ryuqq.setof.domain.session.vo;

/**
 * 업로드 세션 생성 요청 VO.
 *
 * <p>파일 업로드를 위한 Presigned URL 발급 시 필요한 정보를 담습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class UploadSessionRequest {

    private final UploadDirectory directory;
    private final String filename;
    private final String contentType;
    private final long contentLength;

    private UploadSessionRequest(
            UploadDirectory directory, String filename, String contentType, long contentLength) {
        if (directory == null) {
            throw new IllegalArgumentException("업로드 디렉토리는 필수입니다");
        }
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("파일명은 필수입니다");
        }
        this.directory = directory;
        this.filename = filename;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public static UploadSessionRequest of(
            UploadDirectory directory, String filename, String contentType, long contentLength) {
        return new UploadSessionRequest(directory, filename, contentType, contentLength);
    }

    public UploadDirectory directory() {
        return directory;
    }

    public String filename() {
        return filename;
    }

    public String contentType() {
        return contentType;
    }

    public long contentLength() {
        return contentLength;
    }
}

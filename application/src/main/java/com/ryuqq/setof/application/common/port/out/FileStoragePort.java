package com.ryuqq.setof.application.common.port.out;

import java.time.Instant;
import java.util.List;

/**
 * 파일 스토리지 포트 인터페이스
 *
 * <p>파일 업로드/다운로드를 위한 프리사인드 URL 발급을 담당합니다.
 *
 * <p><strong>지원 기능</strong>:
 *
 * <ul>
 *   <li>업로드용 프리사인드 URL 발급
 *   <li>다운로드용 프리사인드 URL 발급
 *   <li>파일 삭제
 * </ul>
 *
 * <p><strong>구현체</strong>:
 *
 * <ul>
 *   <li>FileFlow 서비스 클라이언트
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FileStoragePort {

    /**
     * 업로드용 프리사인드 URL을 발급합니다.
     *
     * @param request 프리사인드 URL 발급 요청
     * @return 프리사인드 URL 정보
     */
    PresignedUrlResponse generateUploadUrl(PresignedUploadUrlRequest request);

    /**
     * 다운로드용 프리사인드 URL을 발급합니다.
     *
     * @param fileAssetId 파일 애셋 ID
     * @param expirationMinutes 만료 시간(분)
     * @return 프리사인드 URL
     */
    String generateDownloadUrl(String fileAssetId, int expirationMinutes);

    /**
     * 파일을 삭제합니다.
     *
     * @param fileAssetId 파일 애셋 ID
     */
    void deleteFile(String fileAssetId);

    /**
     * 여러 파일을 삭제합니다.
     *
     * @param fileAssetIds 파일 애셋 ID 목록
     */
    void deleteFiles(List<String> fileAssetIds);

    /**
     * 프리사인드 업로드 URL 발급 요청
     *
     * @param directory 저장 디렉토리 (예: products/images)
     * @param filename 원본 파일명
     * @param contentType 파일 MIME 타입 (예: image/jpeg, image/png)
     * @param contentLength 파일 크기 (bytes)
     * @param expirationMinutes 만료 시간(분)
     */
    record PresignedUploadUrlRequest(
            String directory,
            String filename,
            String contentType,
            long contentLength,
            int expirationMinutes) {
        /** 기본 만료 시간(15분) 요청 생성 */
        public static PresignedUploadUrlRequest of(
                String directory, String filename, String contentType, long contentLength) {
            return new PresignedUploadUrlRequest(
                    directory, filename, contentType, contentLength, 15);
        }
    }

    /**
     * 프리사인드 URL 응답
     *
     * @param presignedUrl 프리사인드 URL
     * @param fileKey 생성된 파일 키 (S3 Object Key)
     * @param expiresAt 만료 시간
     * @param accessUrl 업로드 완료 후 접근 URL
     */
    record PresignedUrlResponse(
            String presignedUrl, String fileKey, Instant expiresAt, String accessUrl) {}
}

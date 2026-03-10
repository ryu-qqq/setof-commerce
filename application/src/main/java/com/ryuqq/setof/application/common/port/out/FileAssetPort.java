package com.ryuqq.setof.application.common.port.out;

import java.util.List;

/**
 * 파일 자산 관리 아웃바운드 포트.
 *
 * <p>파일 다운로드, 삭제, 외부 URL 처리를 담당합니다.
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>FileflowAssetAdapter - FileFlow SDK 기반
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface FileAssetPort {

    /**
     * 다운로드용 프리사인드 URL을 발급합니다.
     *
     * @param fileAssetId 파일 애셋 ID
     * @param expirationMinutes 만료 시간(분)
     * @return 프리사인드 다운로드 URL
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
     * 외부 URL에서 파일을 다운로드하여 새 CDN에 업로드합니다.
     *
     * @param request 외부 다운로드 요청
     * @return 새 CDN URL 정보
     */
    ExternalDownloadResponse downloadFromExternalUrl(ExternalDownloadRequest request);

    /**
     * 여러 외부 URL에서 파일을 배치로 다운로드합니다.
     *
     * @param requests 외부 다운로드 요청 목록
     * @return 새 CDN URL 정보 목록
     */
    List<ExternalDownloadResponse> downloadFromExternalUrls(List<ExternalDownloadRequest> requests);

    record ExternalDownloadRequest(String sourceUrl, String category, String filename) {}

    record ExternalDownloadResponse(
            String sourceUrl,
            String newCdnUrl,
            String fileAssetId,
            boolean success,
            String errorMessage) {

        public static ExternalDownloadResponse success(
                String sourceUrl, String newCdnUrl, String fileAssetId) {
            return new ExternalDownloadResponse(sourceUrl, newCdnUrl, fileAssetId, true, null);
        }

        public static ExternalDownloadResponse failure(String sourceUrl, String errorMessage) {
            return new ExternalDownloadResponse(sourceUrl, null, null, false, errorMessage);
        }
    }
}

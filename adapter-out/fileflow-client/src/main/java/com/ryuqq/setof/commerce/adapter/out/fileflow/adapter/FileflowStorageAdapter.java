package com.ryuqq.setof.commerce.adapter.out.fileflow.adapter;

import com.ryuqq.fileflow.sdk.client.FileFlowClient;
import com.ryuqq.fileflow.sdk.exception.FileFlowException;
import com.ryuqq.fileflow.sdk.model.asset.DownloadUrlResponse;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadRequest;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadResponse;
import com.ryuqq.setof.application.common.port.out.FileStoragePort;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * FileFlow Storage Adapter
 *
 * <p>FileStoragePort의 구현체입니다. FileFlow SDK를 사용하여 파일 스토리지 API를 호출합니다.
 *
 * <p><strong>책임</strong>:
 *
 * <ul>
 *   <li>업로드용 프리사인드 URL 발급 (UploadSession API)
 *   <li>다운로드용 프리사인드 URL 발급 (FileAsset API)
 *   <li>파일 삭제 (FileAsset API)
 *   <li>SDK 응답을 Port 응답으로 매핑
 * </ul>
 *
 * <p><strong>Zero-Tolerance 규칙 준수</strong>:
 *
 * <ul>
 *   <li>Lombok 금지
 *   <li>@Transactional 금지 (외부 API 호출)
 *   <li>생성자 주입
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FileflowStorageAdapter implements FileStoragePort {

    private static final Logger log = LoggerFactory.getLogger(FileflowStorageAdapter.class);

    private final FileFlowClient fileFlowClient;

    public FileflowStorageAdapter(FileFlowClient fileFlowClient) {
        this.fileFlowClient = fileFlowClient;
    }

    /**
     * 업로드용 프리사인드 URL을 발급합니다.
     *
     * <p>FileFlow SDK의 UploadSession API를 사용하여 프리사인드 PUT URL을 발급받습니다.
     *
     * @param request 프리사인드 URL 발급 요청
     * @return 프리사인드 URL 정보
     */
    @Override
    public PresignedUrlResponse generateUploadUrl(PresignedUploadUrlRequest request) {
        log.debug(
                "FileFlow 업로드 URL 발급 요청: directory={}, filename={}, contentType={}",
                request.directory(),
                request.filename(),
                request.contentType());

        try {
            InitSingleUploadRequest sdkRequest =
                    InitSingleUploadRequest.builder()
                            .filename(request.filename())
                            .contentType(request.contentType())
                            .fileSize(request.contentLength())
                            .category(request.directory())
                            .build();

            InitSingleUploadResponse response =
                    fileFlowClient.uploadSessions().initSingle(sdkRequest);

            log.info(
                    "FileFlow 업로드 URL 발급 성공: sessionId={}, s3Key={}",
                    response.getSessionId(),
                    response.getS3Key());

            return new PresignedUrlResponse(
                    response.getPresignedUrl(),
                    response.getS3Key(),
                    response.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant(),
                    buildAccessUrl(response.getS3Key()));

        } catch (FileFlowException e) {
            log.error(
                    "FileFlow 업로드 URL 발급 실패: directory={}, filename={}, error={}",
                    request.directory(),
                    request.filename(),
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to generate upload URL: " + e.getMessage(), e);
        }
    }

    /**
     * 다운로드용 프리사인드 URL을 발급합니다.
     *
     * <p>FileFlow SDK의 FileAsset API를 사용하여 프리사인드 GET URL을 발급받습니다.
     *
     * @param fileAssetId 파일 애셋 ID
     * @param expirationMinutes 만료 시간(분)
     * @return 프리사인드 다운로드 URL
     */
    @Override
    public String generateDownloadUrl(String fileAssetId, int expirationMinutes) {
        log.debug(
                "FileFlow 다운로드 URL 발급 요청: fileAssetId={}, expirationMinutes={}",
                fileAssetId,
                expirationMinutes);

        try {
            DownloadUrlResponse response =
                    fileFlowClient
                            .fileAssets()
                            .generateDownloadUrl(
                                    fileAssetId, Duration.ofMinutes(expirationMinutes));

            log.info("FileFlow 다운로드 URL 발급 성공: fileAssetId={}", fileAssetId);

            return response.getDownloadUrl();

        } catch (FileFlowException e) {
            log.error(
                    "FileFlow 다운로드 URL 발급 실패: fileAssetId={}, error={}",
                    fileAssetId,
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to generate download URL: " + e.getMessage(), e);
        }
    }

    /**
     * 파일을 삭제합니다 (소프트 삭제).
     *
     * @param fileAssetId 파일 애셋 ID
     */
    @Override
    public void deleteFile(String fileAssetId) {
        log.debug("FileFlow 파일 삭제 요청: fileAssetId={}", fileAssetId);

        try {
            fileFlowClient.fileAssets().delete(fileAssetId);

            log.info("FileFlow 파일 삭제 성공: fileAssetId={}", fileAssetId);

        } catch (FileFlowException e) {
            log.error(
                    "FileFlow 파일 삭제 실패: fileAssetId={}, error={}", fileAssetId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    /**
     * 여러 파일을 삭제합니다 (소프트 삭제).
     *
     * @param fileAssetIds 파일 애셋 ID 목록
     */
    @Override
    public void deleteFiles(List<String> fileAssetIds) {
        if (fileAssetIds == null || fileAssetIds.isEmpty()) {
            log.debug("FileFlow 배치 삭제 요청: 빈 목록, 스킵");
            return;
        }

        log.debug("FileFlow 배치 삭제 요청: count={}", fileAssetIds.size());

        try {
            fileFlowClient.fileAssets().batchDelete(fileAssetIds);

            log.info("FileFlow 배치 삭제 성공: count={}", fileAssetIds.size());

        } catch (FileFlowException e) {
            log.error(
                    "FileFlow 배치 삭제 실패: count={}, error={}",
                    fileAssetIds.size(),
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to delete files: " + e.getMessage(), e);
        }
    }

    /**
     * S3 Key로부터 접근 URL을 생성합니다.
     *
     * <p>업로드 완료 후 파일에 접근하기 위한 URL을 반환합니다. 실제로는 FileFlow의 다운로드 URL 발급 API를 통해 접근해야 합니다.
     *
     * @param s3Key S3 Object Key
     * @return 접근 URL (FileAsset ID 기반)
     */
    private String buildAccessUrl(String s3Key) {
        // S3 Key 자체를 반환 (실제 접근 시 generateDownloadUrl 사용)
        return s3Key;
    }
}

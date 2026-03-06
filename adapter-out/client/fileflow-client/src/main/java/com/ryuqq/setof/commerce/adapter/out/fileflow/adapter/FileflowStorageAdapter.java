package com.ryuqq.setof.commerce.adapter.out.fileflow.adapter;

import com.ryuqq.fileflow.sdk.api.AssetApi;
import com.ryuqq.fileflow.sdk.api.DownloadTaskApi;
import com.ryuqq.fileflow.sdk.api.SingleUploadSessionApi;
import com.ryuqq.fileflow.sdk.exception.FileFlowException;
import com.ryuqq.fileflow.sdk.model.asset.AssetResponse;
import com.ryuqq.fileflow.sdk.model.common.ApiResponse;
import com.ryuqq.fileflow.sdk.model.download.CreateDownloadTaskRequest;
import com.ryuqq.fileflow.sdk.model.download.DownloadTaskResponse;
import com.ryuqq.fileflow.sdk.model.session.CreateSingleUploadSessionRequest;
import com.ryuqq.fileflow.sdk.model.session.SingleUploadSessionResponse;
import com.ryuqq.setof.application.common.port.out.FileStoragePort;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * FileFlow Storage Adapter (v1.0.2)
 *
 * <p>FileStoragePort의 구현체입니다. FileFlow SDK v1.0.2의 개별 API Bean을 주입받아 Spring Boot Starter
 * auto-configuration을 활용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FileflowStorageAdapter implements FileStoragePort {

    private static final Logger log = LoggerFactory.getLogger(FileflowStorageAdapter.class);
    private static final String SOURCE = "setof-commerce";
    private static final String DEFAULT_ACCESS_TYPE = "PRIVATE";

    private final SingleUploadSessionApi singleUploadSessionApi;
    private final AssetApi assetApi;
    private final DownloadTaskApi downloadTaskApi;

    public FileflowStorageAdapter(
            SingleUploadSessionApi singleUploadSessionApi,
            AssetApi assetApi,
            DownloadTaskApi downloadTaskApi) {
        this.singleUploadSessionApi = singleUploadSessionApi;
        this.assetApi = assetApi;
        this.downloadTaskApi = downloadTaskApi;
    }

    @Override
    public PresignedUrlResponse generateUploadUrl(PresignedUploadUrlRequest request) {
        log.debug(
                "FileFlow 업로드 세션 생성 요청: filename={}, contentType={}, purpose={}",
                request.filename(),
                request.contentType(),
                request.directory());

        try {
            CreateSingleUploadSessionRequest sdkRequest =
                    new CreateSingleUploadSessionRequest(
                            request.filename(),
                            request.contentType(),
                            DEFAULT_ACCESS_TYPE,
                            request.directory(),
                            SOURCE);

            ApiResponse<SingleUploadSessionResponse> response =
                    singleUploadSessionApi.create(sdkRequest);
            SingleUploadSessionResponse session = response.data();

            log.info(
                    "FileFlow 업로드 세션 생성 성공: sessionId={}, s3Key={}",
                    session.sessionId(),
                    session.s3Key());

            return new PresignedUrlResponse(
                    session.presignedUrl(),
                    session.s3Key(),
                    Instant.parse(session.expiresAt()),
                    session.s3Key());

        } catch (FileFlowException e) {
            log.error(
                    "FileFlow 업로드 세션 생성 실패: filename={}, error={}",
                    request.filename(),
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to generate upload URL: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateDownloadUrl(String assetId, int expirationMinutes) {
        log.debug("FileFlow 에셋 조회 요청: assetId={}", assetId);

        try {
            ApiResponse<AssetResponse> response = assetApi.get(assetId);
            AssetResponse asset = response.data();

            log.info("FileFlow 에셋 조회 성공: assetId={}, s3Key={}", assetId, asset.s3Key());

            return asset.s3Key();

        } catch (FileFlowException e) {
            log.error("FileFlow 에셋 조회 실패: assetId={}, error={}", assetId, e.getMessage(), e);
            throw new RuntimeException("Failed to get asset: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String assetId) {
        log.debug("FileFlow 파일 삭제 요청: assetId={}", assetId);

        try {
            assetApi.delete(assetId, SOURCE);

            log.info("FileFlow 파일 삭제 성공: assetId={}", assetId);

        } catch (FileFlowException e) {
            log.error("FileFlow 파일 삭제 실패: assetId={}, error={}", assetId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFiles(List<String> assetIds) {
        if (assetIds == null || assetIds.isEmpty()) {
            log.debug("FileFlow 배치 삭제 요청: 빈 목록, 스킵");
            return;
        }

        log.debug("FileFlow 배치 삭제 요청: count={}", assetIds.size());

        for (String assetId : assetIds) {
            try {
                assetApi.delete(assetId, SOURCE);
            } catch (FileFlowException e) {
                log.error(
                        "FileFlow 파일 삭제 실패 (배치): assetId={}, error={}", assetId, e.getMessage(), e);
            }
        }

        log.info("FileFlow 배치 삭제 완료: count={}", assetIds.size());
    }

    @Override
    public ExternalDownloadResponse downloadFromExternalUrl(ExternalDownloadRequest request) {
        log.debug(
                "FileFlow 외부 URL 다운로드 요청: sourceUrl={}, category={}",
                request.sourceUrl(),
                request.category());

        try {
            CreateDownloadTaskRequest sdkRequest =
                    new CreateDownloadTaskRequest(
                            request.sourceUrl(),
                            null,
                            null,
                            DEFAULT_ACCESS_TYPE,
                            request.category(),
                            SOURCE,
                            null);

            ApiResponse<DownloadTaskResponse> response = downloadTaskApi.create(sdkRequest);
            DownloadTaskResponse task = response.data();

            log.info(
                    "FileFlow 다운로드 태스크 생성 성공: taskId={}, sourceUrl={}",
                    task.downloadTaskId(),
                    task.sourceUrl());

            return ExternalDownloadResponse.success(
                    request.sourceUrl(), task.s3Key(), task.downloadTaskId());

        } catch (FileFlowException e) {
            log.error(
                    "FileFlow 외부 URL 다운로드 실패: sourceUrl={}, error={}",
                    request.sourceUrl(),
                    e.getMessage(),
                    e);
            return ExternalDownloadResponse.failure(request.sourceUrl(), e.getMessage());
        }
    }

    @Override
    public List<ExternalDownloadResponse> downloadFromExternalUrls(
            List<ExternalDownloadRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            log.debug("FileFlow 배치 외부 다운로드 요청: 빈 목록, 스킵");
            return List.of();
        }

        log.debug("FileFlow 배치 외부 다운로드 요청: count={}", requests.size());

        List<ExternalDownloadResponse> responses = new ArrayList<>();
        for (ExternalDownloadRequest request : requests) {
            responses.add(downloadFromExternalUrl(request));
        }

        long successCount = responses.stream().filter(ExternalDownloadResponse::success).count();
        log.info(
                "FileFlow 배치 외부 다운로드 완료: total={}, success={}, failed={}",
                requests.size(),
                successCount,
                requests.size() - successCount);

        return responses;
    }
}

package com.ryuqq.setof.commerce.adapter.out.fileflow.adapter;

import com.ryuqq.fileflow.sdk.api.AssetApi;
import com.ryuqq.fileflow.sdk.exception.FileFlowException;
import com.ryuqq.setof.application.common.port.out.FileAssetPort;
import com.ryuqq.setof.commerce.adapter.out.fileflow.config.FileflowClientProperties;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * FileFlow 파일 자산 어댑터.
 *
 * <p>AssetApi를 사용하여 파일 다운로드 URL 발급 및 삭제를 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(prefix = "fileflow", name = "base-url")
public class FileflowAssetAdapter implements FileAssetPort {

    private static final Logger log = LoggerFactory.getLogger(FileflowAssetAdapter.class);
    private static final String SOURCE = "setof-commerce";

    private final AssetApi assetApi;
    private final FileflowClientProperties properties;

    public FileflowAssetAdapter(AssetApi assetApi, FileflowClientProperties properties) {
        this.assetApi = assetApi;
        this.properties = properties;
    }

    @Override
    public String generateDownloadUrl(String fileAssetId, int expirationMinutes) {
        log.debug("FileFlow 다운로드 URL 발급 요청: fileAssetId={}", fileAssetId);

        try {
            var response = assetApi.get(fileAssetId);
            var asset = response.data();
            return buildAccessUrl(asset.s3Key());
        } catch (FileFlowException e) {
            throw new RuntimeException("FileFlow download URL 생성 실패: assetId=" + fileAssetId, e);
        }
    }

    @Override
    public void deleteFile(String fileAssetId) {
        log.debug("FileFlow 파일 삭제 요청: fileAssetId={}", fileAssetId);

        try {
            assetApi.delete(fileAssetId, SOURCE);
            log.info("FileFlow 파일 삭제 성공: fileAssetId={}", fileAssetId);
        } catch (FileFlowException e) {
            log.warn(
                    "FileFlow 파일 삭제 실패: assetId={}, error={}", fileAssetId, e.getErrorMessage(), e);
        }
    }

    @Override
    public void deleteFiles(List<String> fileAssetIds) {
        if (fileAssetIds == null || fileAssetIds.isEmpty()) {
            return;
        }
        for (String fileAssetId : fileAssetIds) {
            deleteFile(fileAssetId);
        }
    }

    @Override
    public ExternalDownloadResponse downloadFromExternalUrl(ExternalDownloadRequest request) {
        log.debug("FileFlow 외부 URL 다운로드는 현재 미지원");
        return ExternalDownloadResponse.failure(request.sourceUrl(), "외부 다운로드 미지원");
    }

    @Override
    public List<ExternalDownloadResponse> downloadFromExternalUrls(
            List<ExternalDownloadRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return List.of();
        }
        List<ExternalDownloadResponse> responses = new ArrayList<>();
        for (ExternalDownloadRequest request : requests) {
            responses.add(downloadFromExternalUrl(request));
        }
        return responses;
    }

    private String buildAccessUrl(String s3Key) {
        String cdnDomain = properties.cdnDomain();
        if (cdnDomain == null || cdnDomain.isBlank() || s3Key == null) {
            return null;
        }
        return "https://" + cdnDomain + "/" + s3Key;
    }
}

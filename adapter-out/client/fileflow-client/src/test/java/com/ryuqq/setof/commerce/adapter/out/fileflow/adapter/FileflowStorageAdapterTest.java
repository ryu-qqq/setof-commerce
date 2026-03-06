package com.ryuqq.setof.commerce.adapter.out.fileflow.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
import com.ryuqq.setof.application.common.port.out.FileStoragePort.ExternalDownloadRequest;
import com.ryuqq.setof.application.common.port.out.FileStoragePort.ExternalDownloadResponse;
import com.ryuqq.setof.application.common.port.out.FileStoragePort.PresignedUploadUrlRequest;
import com.ryuqq.setof.application.common.port.out.FileStoragePort.PresignedUrlResponse;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * FileflowStorageAdapter 단위 테스트 (v1.0.2)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@Tag("adapter")
@Tag("fileflow")
@ExtendWith(MockitoExtension.class)
@DisplayName("FileflowStorageAdapter 단위 테스트")
class FileflowStorageAdapterTest {

    @Mock private SingleUploadSessionApi singleUploadSessionApi;
    @Mock private AssetApi assetApi;
    @Mock private DownloadTaskApi downloadTaskApi;

    private FileflowStorageAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FileflowStorageAdapter(singleUploadSessionApi, assetApi, downloadTaskApi);
    }

    @Nested
    @DisplayName("generateUploadUrl - 업로드 세션 생성")
    class GenerateUploadUrlTests {

        @Test
        @DisplayName("성공 - SDK를 통해 업로드 세션 생성 및 프리사인드 URL 반환")
        void shouldGenerateUploadUrlSuccessfully() {
            // Given
            PresignedUploadUrlRequest request =
                    PresignedUploadUrlRequest.of(
                            "products/images", "product.jpg", "image/jpeg", 1024L);

            SingleUploadSessionResponse session =
                    new SingleUploadSessionResponse(
                            "session-123",
                            "https://s3.amazonaws.com/bucket/presigned-upload-url",
                            "products/images/abc123-product.jpg",
                            "test-bucket",
                            "PRIVATE",
                            "product.jpg",
                            "image/jpeg",
                            "CREATED",
                            Instant.now().plusSeconds(900).toString(),
                            Instant.now().toString());

            given(singleUploadSessionApi.create(any(CreateSingleUploadSessionRequest.class)))
                    .willReturn(new ApiResponse<>(session, Instant.now().toString(), "req-1"));

            // When
            PresignedUrlResponse result = adapter.generateUploadUrl(request);

            // Then
            assertThat(result.presignedUrl())
                    .isEqualTo("https://s3.amazonaws.com/bucket/presigned-upload-url");
            assertThat(result.fileKey()).isEqualTo("products/images/abc123-product.jpg");
            assertThat(result.expiresAt()).isNotNull();
            verify(singleUploadSessionApi).create(any(CreateSingleUploadSessionRequest.class));
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 RuntimeException 전파")
        void shouldThrowRuntimeExceptionWhenFileFlowExceptionOccurs() {
            // Given
            PresignedUploadUrlRequest request =
                    PresignedUploadUrlRequest.of(
                            "products/images", "product.jpg", "image/jpeg", 1024L);

            given(singleUploadSessionApi.create(any(CreateSingleUploadSessionRequest.class)))
                    .willThrow(new FileFlowException("API Error"));

            // When & Then
            assertThatThrownBy(() -> adapter.generateUploadUrl(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to generate upload URL");
        }
    }

    @Nested
    @DisplayName("generateDownloadUrl - 에셋 조회")
    class GenerateDownloadUrlTests {

        @Test
        @DisplayName("성공 - SDK를 통해 에셋 정보 조회 후 s3Key 반환")
        void shouldGenerateDownloadUrlSuccessfully() {
            // Given
            String assetId = "asset-123";
            AssetResponse asset =
                    new AssetResponse(
                            assetId,
                            "products/images/abc123.jpg",
                            "test-bucket",
                            "PRIVATE",
                            "product.jpg",
                            1024L,
                            "image/jpeg",
                            "etag-123",
                            "jpg",
                            "UPLOAD",
                            "session-123",
                            "PRODUCT_IMAGE",
                            "setof-commerce",
                            Instant.now().toString());

            given(assetApi.get(assetId))
                    .willReturn(new ApiResponse<>(asset, Instant.now().toString(), "req-1"));

            // When
            String result = adapter.generateDownloadUrl(assetId, 60);

            // Then
            assertThat(result).isEqualTo("products/images/abc123.jpg");
            verify(assetApi).get(assetId);
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 RuntimeException 전파")
        void shouldThrowRuntimeExceptionWhenFileFlowExceptionOccurs() {
            // Given
            given(assetApi.get("asset-123")).willThrow(new FileFlowException("API Error"));

            // When & Then
            assertThatThrownBy(() -> adapter.generateDownloadUrl("asset-123", 60))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to get asset");
        }
    }

    @Nested
    @DisplayName("deleteFile - 단일 파일 삭제")
    class DeleteFileTests {

        @Test
        @DisplayName("성공 - SDK를 통해 파일 삭제")
        void shouldDeleteFileSuccessfully() {
            // Given
            String assetId = "asset-123";

            // When
            adapter.deleteFile(assetId);

            // Then
            verify(assetApi).delete(assetId, "setof-commerce");
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 RuntimeException 전파")
        void shouldThrowRuntimeExceptionWhenFileFlowExceptionOccurs() {
            // Given
            String assetId = "asset-123";

            org.mockito.Mockito.doThrow(new FileFlowException("API Error"))
                    .when(assetApi)
                    .delete(assetId, "setof-commerce");

            // When & Then
            assertThatThrownBy(() -> adapter.deleteFile(assetId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to delete file");
        }
    }

    @Nested
    @DisplayName("deleteFiles - 배치 파일 삭제")
    class DeleteFilesTests {

        @Test
        @DisplayName("성공 - 개별 삭제 호출로 여러 파일 삭제")
        void shouldDeleteFilesSuccessfully() {
            // Given
            List<String> assetIds = List.of("asset-1", "asset-2", "asset-3");

            // When
            adapter.deleteFiles(assetIds);

            // Then
            verify(assetApi).delete("asset-1", "setof-commerce");
            verify(assetApi).delete("asset-2", "setof-commerce");
            verify(assetApi).delete("asset-3", "setof-commerce");
        }

        @Test
        @DisplayName("빈 목록 - 삭제 스킵")
        void shouldSkipWhenEmptyList() {
            // When
            adapter.deleteFiles(List.of());

            // Then - No interaction with assetApi
        }

        @Test
        @DisplayName("null 목록 - 삭제 스킵")
        void shouldSkipWhenNullList() {
            // When
            adapter.deleteFiles(null);

            // Then - No interaction with assetApi
        }
    }

    @Nested
    @DisplayName("downloadFromExternalUrl - 외부 URL 다운로드")
    class DownloadFromExternalUrlTests {

        @Test
        @DisplayName("성공 - 다운로드 태스크 생성")
        void shouldDownloadFromExternalUrlSuccessfully() {
            // Given
            ExternalDownloadRequest request =
                    new ExternalDownloadRequest(
                            "https://external.com/image.jpg", "products", "image.jpg");

            DownloadTaskResponse task =
                    new DownloadTaskResponse(
                            "task-123",
                            "https://external.com/image.jpg",
                            "products/abc123.jpg",
                            "test-bucket",
                            "PRIVATE",
                            "products",
                            "setof-commerce",
                            "COMPLETED",
                            0,
                            3,
                            null,
                            null,
                            Instant.now().toString(),
                            Instant.now().toString(),
                            Instant.now().toString());

            given(downloadTaskApi.create(any(CreateDownloadTaskRequest.class)))
                    .willReturn(new ApiResponse<>(task, Instant.now().toString(), "req-1"));

            // When
            ExternalDownloadResponse result = adapter.downloadFromExternalUrl(request);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.sourceUrl()).isEqualTo("https://external.com/image.jpg");
            assertThat(result.newCdnUrl()).isEqualTo("products/abc123.jpg");
            assertThat(result.fileAssetId()).isEqualTo("task-123");
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 실패 응답 반환")
        void shouldReturnFailureWhenFileFlowExceptionOccurs() {
            // Given
            ExternalDownloadRequest request =
                    new ExternalDownloadRequest(
                            "https://external.com/image.jpg", "products", "image.jpg");

            given(downloadTaskApi.create(any(CreateDownloadTaskRequest.class)))
                    .willThrow(new FileFlowException("API Error"));

            // When
            ExternalDownloadResponse result = adapter.downloadFromExternalUrl(request);

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.errorMessage()).isEqualTo("API Error");
        }
    }
}

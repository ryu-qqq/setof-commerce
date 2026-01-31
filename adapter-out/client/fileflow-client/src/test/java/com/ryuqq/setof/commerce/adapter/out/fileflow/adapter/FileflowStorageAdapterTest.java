package com.ryuqq.setof.commerce.adapter.out.fileflow.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.fileflow.sdk.api.FileAssetApi;
import com.ryuqq.fileflow.sdk.api.UploadSessionApi;
import com.ryuqq.fileflow.sdk.client.FileFlowClient;
import com.ryuqq.fileflow.sdk.exception.FileFlowException;
import com.ryuqq.fileflow.sdk.model.asset.DownloadUrlResponse;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadRequest;
import com.ryuqq.fileflow.sdk.model.session.InitSingleUploadResponse;
import com.ryuqq.setof.application.common.port.out.FileStoragePort.PresignedUploadUrlRequest;
import com.ryuqq.setof.application.common.port.out.FileStoragePort.PresignedUrlResponse;
import java.time.Duration;
import java.time.LocalDateTime;
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
 * FileflowStorageAdapter 단위 테스트
 *
 * <p>FileFlow SDK를 사용한 파일 스토리지 Adapter의 비즈니스 로직을 검증합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>generateUploadUrl - 업로드 프리사인드 URL 발급
 *   <li>generateDownloadUrl - 다운로드 프리사인드 URL 발급
 *   <li>deleteFile - 단일 파일 삭제
 *   <li>deleteFiles - 배치 파일 삭제
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Tag("unit")
@Tag("adapter")
@Tag("fileflow")
@ExtendWith(MockitoExtension.class)
@DisplayName("FileflowStorageAdapter 단위 테스트")
class FileflowStorageAdapterTest {

    @Mock private FileFlowClient fileFlowClient;

    @Mock private UploadSessionApi uploadSessionApi;

    @Mock private FileAssetApi fileAssetApi;

    private FileflowStorageAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FileflowStorageAdapter(fileFlowClient);
    }

    @Nested
    @DisplayName("generateUploadUrl - 업로드 URL 발급")
    class GenerateUploadUrlTests {

        @Test
        @DisplayName("성공 - SDK를 통해 업로드 프리사인드 URL 발급")
        void shouldGenerateUploadUrlSuccessfully() {
            // Given
            PresignedUploadUrlRequest request =
                    PresignedUploadUrlRequest.of(
                            "products/images", "product.jpg", "image/jpeg", 1024L);

            InitSingleUploadResponse sdkResponse =
                    new InitSingleUploadResponse(
                            "session-123",
                            "https://s3.amazonaws.com/bucket/presigned-upload-url",
                            LocalDateTime.now().plusMinutes(15),
                            "products/images/abc123-product.jpg");

            given(fileFlowClient.uploadSessions()).willReturn(uploadSessionApi);
            given(uploadSessionApi.initSingle(any(InitSingleUploadRequest.class)))
                    .willReturn(sdkResponse);

            // When
            PresignedUrlResponse result = adapter.generateUploadUrl(request);

            // Then
            assertThat(result.presignedUrl())
                    .isEqualTo("https://s3.amazonaws.com/bucket/presigned-upload-url");
            assertThat(result.fileKey()).isEqualTo("products/images/abc123-product.jpg");
            assertThat(result.expiresAt()).isNotNull();
            verify(uploadSessionApi).initSingle(any(InitSingleUploadRequest.class));
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 RuntimeException 전파")
        void shouldThrowRuntimeExceptionWhenFileFlowExceptionOccurs() {
            // Given
            PresignedUploadUrlRequest request =
                    PresignedUploadUrlRequest.of(
                            "products/images", "product.jpg", "image/jpeg", 1024L);

            given(fileFlowClient.uploadSessions()).willReturn(uploadSessionApi);
            given(uploadSessionApi.initSingle(any(InitSingleUploadRequest.class)))
                    .willThrow(new FileFlowException("API Error"));

            // When & Then
            assertThatThrownBy(() -> adapter.generateUploadUrl(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to generate upload URL");
        }
    }

    @Nested
    @DisplayName("generateDownloadUrl - 다운로드 URL 발급")
    class GenerateDownloadUrlTests {

        @Test
        @DisplayName("성공 - SDK를 통해 다운로드 프리사인드 URL 발급")
        void shouldGenerateDownloadUrlSuccessfully() {
            // Given
            String fileAssetId = "asset-123";
            int expirationMinutes = 60;
            DownloadUrlResponse sdkResponse =
                    new DownloadUrlResponse(
                            fileAssetId,
                            "https://s3.amazonaws.com/bucket/presigned-download-url",
                            LocalDateTime.now().plusMinutes(expirationMinutes));

            given(fileFlowClient.fileAssets()).willReturn(fileAssetApi);
            given(fileAssetApi.generateDownloadUrl(eq(fileAssetId), any(Duration.class)))
                    .willReturn(sdkResponse);

            // When
            String result = adapter.generateDownloadUrl(fileAssetId, expirationMinutes);

            // Then
            assertThat(result).isEqualTo("https://s3.amazonaws.com/bucket/presigned-download-url");
            verify(fileAssetApi)
                    .generateDownloadUrl(
                            eq(fileAssetId), eq(Duration.ofMinutes(expirationMinutes)));
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 RuntimeException 전파")
        void shouldThrowRuntimeExceptionWhenFileFlowExceptionOccurs() {
            // Given
            String fileAssetId = "asset-123";

            given(fileFlowClient.fileAssets()).willReturn(fileAssetApi);
            given(fileAssetApi.generateDownloadUrl(anyString(), any(Duration.class)))
                    .willThrow(new FileFlowException("API Error"));

            // When & Then
            assertThatThrownBy(() -> adapter.generateDownloadUrl(fileAssetId, 60))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to generate download URL");
        }
    }

    @Nested
    @DisplayName("deleteFile - 단일 파일 삭제")
    class DeleteFileTests {

        @Test
        @DisplayName("성공 - SDK를 통해 파일 삭제")
        void shouldDeleteFileSuccessfully() {
            // Given
            String fileAssetId = "asset-123";

            given(fileFlowClient.fileAssets()).willReturn(fileAssetApi);

            // When
            adapter.deleteFile(fileAssetId);

            // Then
            verify(fileAssetApi).delete(fileAssetId);
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 RuntimeException 전파")
        void shouldThrowRuntimeExceptionWhenFileFlowExceptionOccurs() {
            // Given
            String fileAssetId = "asset-123";

            given(fileFlowClient.fileAssets()).willReturn(fileAssetApi);
            org.mockito.Mockito.doThrow(new FileFlowException("API Error"))
                    .when(fileAssetApi)
                    .delete(fileAssetId);

            // When & Then
            assertThatThrownBy(() -> adapter.deleteFile(fileAssetId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to delete file");
        }
    }

    @Nested
    @DisplayName("deleteFiles - 배치 파일 삭제")
    class DeleteFilesTests {

        @Test
        @DisplayName("성공 - SDK를 통해 여러 파일 삭제")
        void shouldDeleteFilesSuccessfully() {
            // Given
            List<String> fileAssetIds = List.of("asset-1", "asset-2", "asset-3");

            given(fileFlowClient.fileAssets()).willReturn(fileAssetApi);

            // When
            adapter.deleteFiles(fileAssetIds);

            // Then
            verify(fileAssetApi).batchDelete(fileAssetIds);
        }

        @Test
        @DisplayName("빈 목록 - 삭제 스킵")
        void shouldSkipWhenEmptyList() {
            // Given
            List<String> fileAssetIds = List.of();

            // When
            adapter.deleteFiles(fileAssetIds);

            // Then - No interaction with fileFlowClient
        }

        @Test
        @DisplayName("null 목록 - 삭제 스킵")
        void shouldSkipWhenNullList() {
            // When
            adapter.deleteFiles(null);

            // Then - No interaction with fileFlowClient
        }

        @Test
        @DisplayName("실패 - FileFlowException 발생 시 RuntimeException 전파")
        void shouldThrowRuntimeExceptionWhenFileFlowExceptionOccurs() {
            // Given
            List<String> fileAssetIds = List.of("asset-1", "asset-2");

            given(fileFlowClient.fileAssets()).willReturn(fileAssetApi);
            org.mockito.Mockito.doThrow(new FileFlowException("API Error"))
                    .when(fileAssetApi)
                    .batchDelete(fileAssetIds);

            // When & Then
            assertThatThrownBy(() -> adapter.deleteFiles(fileAssetIds))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Failed to delete files");
        }
    }
}

package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.command.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response.ProductImageListV2ApiResponse;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.port.in.command.DeleteProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("ProductImage Admin V2 Controller 통합 테스트")
class ProductImageAdminV2ControllerTest extends ApiIntegrationTestSupport {

    @Autowired private GetProductImageUseCase getProductImageUseCase;

    @Autowired private UpdateProductImageUseCase updateProductImageUseCase;

    @Autowired private DeleteProductImageUseCase deleteProductImageUseCase;

    @Nested
    @DisplayName("GET " + ApiV2Paths.ProductImages.BASE)
    class GetProductImages {

        @Test
        @DisplayName("상품이미지 목록 조회 성공")
        void getProductImages_success() {
            // Given
            Long productGroupId = 1L;
            List<ProductImageResponse> mockResponses =
                    List.of(
                            new ProductImageResponse(
                                    1L,
                                    productGroupId,
                                    "MAIN",
                                    "https://origin.com/main.jpg",
                                    "https://cdn.com/main.jpg",
                                    1,
                                    Instant.now()),
                            new ProductImageResponse(
                                    2L,
                                    productGroupId,
                                    "SUB",
                                    "https://origin.com/sub.jpg",
                                    "https://cdn.com/sub.jpg",
                                    2,
                                    Instant.now()));

            given(getProductImageUseCase.getByProductGroupId(eq(productGroupId)))
                    .willReturn(mockResponses);

            // When
            ResponseEntity<ApiResponse<ProductImageListV2ApiResponse>> response =
                    get(
                            ApiV2Paths.ProductImages.BASE.replace(
                                    "{productGroupId}", productGroupId.toString()),
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().items()).hasSize(2);
            assertThat(response.getBody().data().totalCount()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("PUT " + ApiV2Paths.ProductImages.UPDATE)
    class UpdateProductImage {

        @Test
        @DisplayName("상품이미지 수정 성공")
        void updateProductImage_success() {
            // Given
            Long productGroupId = 1L;
            Long imageId = 10L;
            UpdateProductImageV2ApiRequest request =
                    new UpdateProductImageV2ApiRequest("MAIN", "https://cdn.com/updated.jpg", 1);

            // When
            String url =
                    ApiV2Paths.ProductImages.BASE.replace(
                                    "{productGroupId}", productGroupId.toString())
                            + "/"
                            + imageId;
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(updateProductImageUseCase).update(any(UpdateProductImageCommand.class));
        }

        @Test
        @DisplayName("상품이미지 수정 실패 - 필수값 누락")
        void updateProductImage_validationFailed() {
            // Given
            Long productGroupId = 1L;
            Long imageId = 10L;
            UpdateProductImageV2ApiRequest request =
                    new UpdateProductImageV2ApiRequest("", null, -1);

            // When
            String url =
                    ApiV2Paths.ProductImages.BASE.replace(
                                    "{productGroupId}", productGroupId.toString())
                            + "/"
                            + imageId;
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("DELETE " + ApiV2Paths.ProductImages.DELETE)
    class DeleteProductImage {

        @Test
        @DisplayName("상품이미지 삭제 성공")
        void deleteProductImage_success() {
            // Given
            Long productGroupId = 1L;
            Long imageId = 10L;

            // When
            String url =
                    ApiV2Paths.ProductImages.BASE.replace(
                                    "{productGroupId}", productGroupId.toString())
                            + "/"
                            + imageId;
            ResponseEntity<ApiResponse<Void>> response =
                    delete(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(deleteProductImageUseCase).delete(eq(imageId));
        }
    }
}

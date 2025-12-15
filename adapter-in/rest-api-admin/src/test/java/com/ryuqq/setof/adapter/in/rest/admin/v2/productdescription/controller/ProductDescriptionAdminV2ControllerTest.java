package com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.command.UpdateProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.response.ProductDescriptionV2ApiResponse;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.response.DescriptionImageResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("ProductDescription Admin V2 Controller 통합 테스트")
class ProductDescriptionAdminV2ControllerTest extends ApiIntegrationTestSupport {

    @Autowired private GetProductDescriptionUseCase getProductDescriptionUseCase;

    @Autowired private UpdateProductDescriptionUseCase updateProductDescriptionUseCase;

    @Nested
    @DisplayName("GET " + ApiV2Paths.ProductDescriptions.BASE)
    class GetProductDescription {

        @Test
        @DisplayName("상품설명 조회 성공")
        void getProductDescription_success() {
            // Given
            Long productGroupId = 1L;
            ProductDescriptionResponse mockResponse =
                    new ProductDescriptionResponse(
                            100L,
                            productGroupId,
                            "<p>상품 설명</p>",
                            List.of(
                                    new DescriptionImageResponse(
                                            1,
                                            "https://origin.com/img.jpg",
                                            "https://cdn.com/img.jpg",
                                            Instant.now(),
                                            true)),
                            true,
                            true,
                            Instant.now(),
                            Instant.now());

            given(getProductDescriptionUseCase.findByProductGroupId(eq(productGroupId)))
                    .willReturn(Optional.of(mockResponse));

            // When
            ResponseEntity<ApiResponse<ProductDescriptionV2ApiResponse>> response =
                    get(
                            ApiV2Paths.ProductDescriptions.BASE.replace(
                                    "{productGroupId}", productGroupId.toString()),
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().productDescriptionId()).isEqualTo(100L);
            assertThat(response.getBody().data().htmlContent()).isEqualTo("<p>상품 설명</p>");
        }
    }

    @Nested
    @DisplayName("PUT " + ApiV2Paths.ProductDescriptions.BASE)
    class UpdateProductDescription {

        @Test
        @DisplayName("상품설명 수정 성공")
        void updateProductDescription_success() {
            // Given
            Long productGroupId = 1L;
            UpdateProductDescriptionV2ApiRequest request =
                    new UpdateProductDescriptionV2ApiRequest(
                            100L,
                            "<p>수정된 설명</p>",
                            List.of(
                                    new UpdateProductDescriptionV2ApiRequest
                                            .DescriptionImageV2ApiRequest(
                                            1,
                                            "https://origin.com/new.jpg",
                                            "https://cdn.com/new.jpg")));

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            ApiV2Paths.ProductDescriptions.BASE.replace(
                                    "{productGroupId}", productGroupId.toString()),
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(updateProductDescriptionUseCase)
                    .execute(any(UpdateProductDescriptionCommand.class));
        }

        @Test
        @DisplayName("상품설명 수정 실패 - 필수값 누락")
        void updateProductDescription_validationFailed() {
            // Given
            Long productGroupId = 1L;
            UpdateProductDescriptionV2ApiRequest request =
                    new UpdateProductDescriptionV2ApiRequest(null, "", null);

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            ApiV2Paths.ProductDescriptions.BASE.replace(
                                    "{productGroupId}", productGroupId.toString()),
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}

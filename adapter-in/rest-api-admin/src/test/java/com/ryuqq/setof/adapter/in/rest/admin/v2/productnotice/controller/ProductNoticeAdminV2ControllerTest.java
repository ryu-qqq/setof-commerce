package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.response.ProductNoticeV2ApiResponse;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.response.NoticeItemResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import com.ryuqq.setof.application.productnotice.port.in.query.GetProductNoticeUseCase;
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

@DisplayName("ProductNotice Admin V2 Controller 통합 테스트")
class ProductNoticeAdminV2ControllerTest extends ApiIntegrationTestSupport {

    @Autowired private GetProductNoticeUseCase getProductNoticeUseCase;

    @Autowired private UpdateProductNoticeUseCase updateProductNoticeUseCase;

    @Nested
    @DisplayName("GET " + ApiV2Paths.ProductNotices.BASE)
    class GetProductNotice {

        @Test
        @DisplayName("상품고시 조회 성공")
        void getProductNotice_success() {
            // Given
            Long productGroupId = 1L;
            ProductNoticeResponse mockResponse =
                    new ProductNoticeResponse(
                            100L,
                            productGroupId,
                            1L,
                            List.of(
                                    new NoticeItemResponse("material", "면 100%", 1, true),
                                    new NoticeItemResponse("origin", "대한민국", 2, true)),
                            2,
                            Instant.now(),
                            Instant.now());

            given(getProductNoticeUseCase.findByProductGroupId(eq(productGroupId)))
                    .willReturn(Optional.of(mockResponse));

            // When
            ResponseEntity<ApiResponse<ProductNoticeV2ApiResponse>> response =
                    get(
                            ApiV2Paths.ProductNotices.BASE.replace(
                                    "{productGroupId}", productGroupId.toString()),
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().productNoticeId()).isEqualTo(100L);
            assertThat(response.getBody().data().items()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("PUT " + ApiV2Paths.ProductNotices.BASE)
    class UpdateProductNotice {

        @Test
        @DisplayName("상품고시 수정 성공")
        void updateProductNotice_success() {
            // Given
            Long productGroupId = 1L;
            UpdateProductNoticeV2ApiRequest request =
                    new UpdateProductNoticeV2ApiRequest(
                            100L,
                            List.of(
                                    new UpdateProductNoticeV2ApiRequest.NoticeItemV2ApiRequest(
                                            "material", "폴리에스터 100%", 1),
                                    new UpdateProductNoticeV2ApiRequest.NoticeItemV2ApiRequest(
                                            "origin", "중국", 2)));

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            ApiV2Paths.ProductNotices.BASE.replace(
                                    "{productGroupId}", productGroupId.toString()),
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(updateProductNoticeUseCase).execute(any(UpdateProductNoticeCommand.class));
        }

        @Test
        @DisplayName("상품고시 수정 실패 - 필수값 누락")
        void updateProductNotice_validationFailed() {
            // Given
            Long productGroupId = 1L;
            UpdateProductNoticeV2ApiRequest request =
                    new UpdateProductNoticeV2ApiRequest(null, List.of());

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            ApiV2Paths.ProductNotices.BASE.replace(
                                    "{productGroupId}", productGroupId.toString()),
                            request,
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}

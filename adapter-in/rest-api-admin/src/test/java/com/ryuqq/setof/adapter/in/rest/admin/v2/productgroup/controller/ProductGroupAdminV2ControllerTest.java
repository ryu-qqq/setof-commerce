package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductDescriptionV2ApiRequest.DescriptionImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest.NoticeItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductSkuV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest.UpdateProductSkuV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupListV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupV2ApiResponse;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupStatusUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * ProductGroup Admin V2 Controller 통합 테스트
 *
 * <p>상품그룹 관리 API 통합 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("ProductGroupAdminV2Controller 통합 테스트")
class ProductGroupAdminV2ControllerTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/product-groups";

    @Autowired private GetProductGroupUseCase getProductGroupUseCase;

    @Autowired private GetProductGroupsUseCase getProductGroupsUseCase;

    @Autowired private RegisterFullProductUseCase registerFullProductUseCase;

    @Autowired private UpdateFullProductUseCase updateFullProductUseCase;

    @Autowired private UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase;

    @Nested
    @DisplayName("GET /api/v2/admin/product-groups/{productGroupId} - 단일 조회")
    class GetProductGroupTest {

        @Test
        @DisplayName("존재하는 상품그룹 ID로 조회하면 200 OK와 상품그룹 정보를 반환한다")
        void getProductGroup_success() {
            // Given
            Long productGroupId = 1L;
            ProductGroupResponse response =
                    ProductGroupResponse.of(
                            productGroupId,
                            1L,
                            100L,
                            10L,
                            "프리미엄 코튼 티셔츠",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(39000),
                            "APPROVED",
                            1L,
                            1L,
                            List.of());

            given(getProductGroupUseCase.execute(productGroupId)).willReturn(response);

            // When
            ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> result =
                    get(
                            BASE_URL + "/{productGroupId}",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().data().productGroupId()).isEqualTo(productGroupId);
            assertThat(result.getBody().data().name()).isEqualTo("프리미엄 코튼 티셔츠");

            verify(getProductGroupUseCase).execute(productGroupId);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/admin/product-groups - 목록 조회")
    class GetProductGroupsTest {

        @Test
        @DisplayName("검색 조건으로 목록을 조회하면 200 OK와 목록을 반환한다")
        void getProductGroups_success() {
            // Given
            List<ProductGroupSummaryResponse> responses =
                    List.of(
                            ProductGroupSummaryResponse.of(
                                    1L,
                                    1L,
                                    "상품그룹1",
                                    "TWO_LEVEL",
                                    BigDecimal.valueOf(30000),
                                    "APPROVED",
                                    5),
                            ProductGroupSummaryResponse.of(
                                    2L,
                                    1L,
                                    "상품그룹2",
                                    "ONE_LEVEL",
                                    BigDecimal.valueOf(25000),
                                    "APPROVED",
                                    3));

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(responses);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class))).willReturn(2L);

            // When
            ResponseEntity<ApiResponse<ProductGroupListV2ApiResponse>> result =
                    get(
                            BASE_URL + "?sellerId=1&page=0&size=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().data().items()).hasSize(2);
            assertThat(result.getBody().data().totalCount()).isEqualTo(2L);

            verify(getProductGroupsUseCase).execute(any(ProductGroupSearchQuery.class));
            verify(getProductGroupsUseCase).count(any(ProductGroupSearchQuery.class));
        }
    }

    @Nested
    @DisplayName("POST /api/v2/admin/product-groups - 상품그룹 등록")
    class RegisterProductGroupTest {

        @Test
        @DisplayName("유효한 요청으로 상품그룹을 등록하면 201 Created와 생성된 ID를 반환한다")
        void registerProductGroup_success() {
            // Given
            Long expectedProductGroupId = 1L;

            RegisterProductGroupV2ApiRequest request = createValidRegisterRequest();

            given(
                            registerFullProductUseCase.registerFullProduct(
                                    any(RegisterFullProductCommand.class)))
                    .willReturn(expectedProductGroupId);

            // When
            ResponseEntity<ApiResponse<Long>> result =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().data()).isEqualTo(expectedProductGroupId);

            verify(registerFullProductUseCase)
                    .registerFullProduct(any(RegisterFullProductCommand.class));
        }

        @Test
        @DisplayName("필수값이 누락되면 400 Bad Request를 반환한다")
        void registerProductGroup_invalidRequest_returnsBadRequest() {
            // Given - 필수값 누락된 요청
            RegisterProductGroupV2ApiRequest request =
                    new RegisterProductGroupV2ApiRequest(
                            null, // sellerId 누락
                            null, // categoryId 누락
                            null, // brandId 누락
                            "", // name 빈값
                            null, // optionType 누락
                            null, // regularPrice 누락
                            null, // currentPrice 누락
                            null, null, List.of(), // products 빈 목록
                            List.of(), // images 빈 목록
                            null, null);

            // When
            ResponseEntity<ApiResponse<Long>> result =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("PUT /api/v2/admin/product-groups/{productGroupId} - 상품그룹 수정")
    class UpdateProductGroupTest {

        @Test
        @DisplayName("유효한 요청으로 상품그룹을 수정하면 200 OK를 반환한다")
        void updateProductGroup_success() {
            // Given
            Long productGroupId = 1L;
            UpdateProductGroupV2ApiRequest request = createValidUpdateRequest();

            // When
            ResponseEntity<ApiResponse<Void>> result =
                    put(
                            BASE_URL + "/{productGroupId}",
                            request,
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateFullProductUseCase).updateFullProduct(any(UpdateFullProductCommand.class));
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/admin/product-groups/{productGroupId}/status - 상태 변경")
    class UpdateProductGroupStatusTest {

        @Test
        @DisplayName("유효한 요청으로 상태를 변경하면 200 OK를 반환한다")
        void updateProductGroupStatus_success() {
            // Given
            Long productGroupId = 1L;
            Long sellerId = 1L;
            UpdateProductGroupStatusV2ApiRequest request =
                    new UpdateProductGroupStatusV2ApiRequest("ACTIVE");

            // When
            ResponseEntity<ApiResponse<Void>> result =
                    patch(
                            BASE_URL + "/{productGroupId}/status?sellerId=" + sellerId,
                            request,
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateProductGroupStatusUseCase)
                    .execute(any(UpdateProductGroupStatusCommand.class));
        }

        @Test
        @DisplayName("상태값이 비어있으면 400 Bad Request를 반환한다")
        void updateProductGroupStatus_emptyStatus_returnsBadRequest() {
            // Given
            Long productGroupId = 1L;
            Long sellerId = 1L;
            UpdateProductGroupStatusV2ApiRequest request =
                    new UpdateProductGroupStatusV2ApiRequest("");

            // When
            ResponseEntity<ApiResponse<Void>> result =
                    patch(
                            BASE_URL + "/{productGroupId}/status?sellerId=" + sellerId,
                            request,
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ========== Helper Methods ==========

    private RegisterProductGroupV2ApiRequest createValidRegisterRequest() {
        return new RegisterProductGroupV2ApiRequest(
                1L, // sellerId
                100L, // categoryId
                10L, // brandId
                "프리미엄 코튼 티셔츠", // name
                "TWO_LEVEL", // optionType
                BigDecimal.valueOf(50000), // regularPrice
                BigDecimal.valueOf(39000), // currentPrice
                1L, // shippingPolicyId
                1L, // refundPolicyId
                List.of(
                        new ProductSkuV2ApiRequest("색상", "블랙", "사이즈", "M", BigDecimal.ZERO, 100),
                        new ProductSkuV2ApiRequest("색상", "블랙", "사이즈", "L", BigDecimal.ZERO, 50)),
                List.of(
                        new ProductImageV2ApiRequest(
                                "MAIN",
                                "https://cdn.example.com/main.jpg",
                                "https://cdn.example.com/main.jpg",
                                1)),
                new ProductDescriptionV2ApiRequest(
                        "<p>상품 상세 설명</p>",
                        List.of(
                                new DescriptionImageV2ApiRequest(
                                        1,
                                        "https://cdn.example.com/desc.jpg",
                                        "https://cdn.example.com/desc.jpg"))),
                new ProductNoticeV2ApiRequest(
                        1L, List.of(new NoticeItemV2ApiRequest("material", "면 100%", 1))));
    }

    private UpdateProductGroupV2ApiRequest createValidUpdateRequest() {
        return new UpdateProductGroupV2ApiRequest(
                100L, // categoryId
                10L, // brandId
                "프리미엄 코튼 티셔츠 (수정)", // name
                "TWO_LEVEL", // optionType
                BigDecimal.valueOf(55000), // regularPrice
                BigDecimal.valueOf(42000), // currentPrice
                "APPROVED", // status
                1L, // shippingPolicyId
                1L, // refundPolicyId
                List.of(
                        new UpdateProductSkuV2ApiRequest(
                                "색상", "블랙", "사이즈", "M", BigDecimal.ZERO, 150)),
                List.of(
                        new UpdateProductImageV2ApiRequest(
                                1L,
                                "MAIN",
                                "https://cdn.example.com/main.jpg",
                                "https://cdn.example.com/main.jpg",
                                1)),
                null,
                null);
    }
}

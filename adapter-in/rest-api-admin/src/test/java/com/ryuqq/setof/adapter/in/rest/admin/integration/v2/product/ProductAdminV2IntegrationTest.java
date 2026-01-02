package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.product;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.ProductAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.command.UpdateProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.response.ProductDescriptionV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupListV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.command.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response.ProductImageListV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.response.ProductNoticeV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.response.ProductStockV2ApiResponse;
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
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import com.ryuqq.setof.application.productnotice.port.in.query.GetProductNoticeUseCase;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import com.ryuqq.setof.application.productstock.port.in.query.GetProductStockUseCase;
import com.ryuqq.setof.domain.product.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Product Admin V2 API 통합 테스트
 *
 * <p>V2 Product Admin API의 통합 테스트를 수행합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>ProductGroup - 상품그룹 조회/등록/수정/상태변경
 *   <li>ProductDescription - 상품설명 조회/수정
 *   <li>ProductImage - 상품이미지 조회/수정/삭제
 *   <li>ProductNotice - 상품고시 조회/수정
 *   <li>ProductStock - 재고 조회/설정
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Admin Product V2 API 통합 테스트")
class ProductAdminV2IntegrationTest extends ApiIntegrationTestSupport {

    // ============================================================
    // ProductGroup UseCases
    // ============================================================

    @Autowired private GetProductGroupUseCase getProductGroupUseCase;

    @Autowired private GetProductGroupsUseCase getProductGroupsUseCase;

    @Autowired private RegisterFullProductUseCase registerFullProductUseCase;

    @Autowired private UpdateFullProductUseCase updateFullProductUseCase;

    @Autowired private UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase;

    // ============================================================
    // ProductDescription UseCases
    // ============================================================

    @Autowired private GetProductDescriptionUseCase getProductDescriptionUseCase;

    @Autowired private UpdateProductDescriptionUseCase updateProductDescriptionUseCase;

    // ============================================================
    // ProductImage UseCases
    // ============================================================

    @Autowired private GetProductImageUseCase getProductImageUseCase;

    @Autowired private UpdateProductImageUseCase updateProductImageUseCase;

    // ============================================================
    // ProductNotice UseCases
    // ============================================================

    @Autowired private GetProductNoticeUseCase getProductNoticeUseCase;

    @Autowired private UpdateProductNoticeUseCase updateProductNoticeUseCase;

    // ============================================================
    // ProductStock UseCases
    // ============================================================

    @Autowired private GetProductStockUseCase getProductStockUseCase;

    // ============================================================
    // ProductGroup Query Tests
    // ============================================================

    @Nested
    @DisplayName("상품그룹 단일 조회 (V2)")
    class GetProductGroupTest {

        @Test
        @DisplayName("APRD-V2-001: 유효한 ID로 상품그룹을 조회하면 성공한다")
        void getProductGroup_withValidId_success() {
            // given
            ProductGroupResponse productGroupResponse = createProductGroupResponse();

            given(getProductGroupUseCase.execute(DEFAULT_PRODUCT_GROUP_ID))
                    .willReturn(productGroupResponse);

            String url = ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductGroupV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().productGroupId())
                    .isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V2-002: 존재하지 않는 ID로 조회하면 404를 반환한다")
        void getProductGroup_withNonExistentId_returns404() {
            // given
            given(getProductGroupUseCase.execute(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .willThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID));

            String url = ApiV2Paths.ProductGroups.BASE + "/" + NON_EXISTENT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductGroupV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("상품그룹 목록 조회 (V2)")
    class GetProductGroupsTest {

        @Test
        @DisplayName("APRD-V2-003: 필터 없이 목록을 조회하면 성공한다")
        void getProductGroups_withoutFilters_success() {
            // given
            List<ProductGroupSummaryResponse> summaries = createProductGroupSummaryResponses(5);

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(summaries);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class))).willReturn(5L);

            // when
            ResponseEntity<ApiResponse<ProductGroupListV2ApiResponse>> response =
                    get(
                            ApiV2Paths.ProductGroups.BASE,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductGroupListV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().items()).hasSize(5);
        }

        @Test
        @DisplayName("APRD-V2-004: sellerId로 필터링하면 해당 판매자의 상품만 반환된다")
        void getProductGroups_withSellerIdFilter_success() {
            // given
            List<ProductGroupSummaryResponse> summaries =
                    List.of(createProductGroupSummaryResponse());

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(summaries);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class))).willReturn(1L);

            String url = ApiV2Paths.ProductGroups.BASE + "?sellerId=" + DEFAULT_SELLER_ID;

            // when
            ResponseEntity<ApiResponse<ProductGroupListV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductGroupListV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().items()).hasSize(1);
        }

        @Test
        @DisplayName("APRD-V2-005: 페이지네이션이 정상 동작한다")
        void getProductGroups_withPagination_success() {
            // given
            List<ProductGroupSummaryResponse> summaries = createProductGroupSummaryResponses(10);

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(summaries);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class)))
                    .willReturn(50L);

            String url = ApiV2Paths.ProductGroups.BASE + "?pageNumber=0&pageSize=10";

            // when
            ResponseEntity<ApiResponse<ProductGroupListV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductGroupListV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().items()).hasSize(10);
            assertThat(response.getBody().data().totalCount()).isEqualTo(50L);
        }
    }

    // ============================================================
    // ProductGroup Command Tests
    // ============================================================

    @Nested
    @DisplayName("상품그룹 등록 (V2)")
    class RegisterProductGroupTest {

        @Test
        @DisplayName("APRD-V2-006: 유효한 요청으로 상품그룹을 등록하면 201을 반환한다")
        void registerProductGroup_withValidRequest_returns201() {
            // given
            RegisterProductGroupV2ApiRequest request = createRegisterProductGroupV2Request();

            given(
                            registerFullProductUseCase.registerFullProduct(
                                    any(RegisterFullProductCommand.class)))
                    .willReturn(DEFAULT_PRODUCT_GROUP_ID);

            // when
            ResponseEntity<ApiResponse<Long>> response =
                    post(
                            ApiV2Paths.ProductGroups.BASE,
                            request,
                            new ParameterizedTypeReference<ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V2-007: 필수 필드 누락 시 400을 반환한다")
        void registerProductGroup_withMissingRequiredFields_returns400() {
            // given - sellerId 누락
            RegisterProductGroupV2ApiRequest request =
                    new RegisterProductGroupV2ApiRequest(
                            null, // sellerId 누락
                            DEFAULT_CATEGORY_ID,
                            DEFAULT_BRAND_ID,
                            DEFAULT_PRODUCT_GROUP_NAME,
                            DEFAULT_OPTION_TYPE,
                            DEFAULT_REGULAR_PRICE,
                            DEFAULT_CURRENT_PRICE,
                            null, // shippingPolicyId
                            null, // refundPolicyId
                            List.of(createProductSkuV2Request()),
                            List.of(createProductImageV2Request()),
                            createProductDescriptionV2Request(),
                            createProductNoticeV2Request());

            // when
            ResponseEntity<ApiResponse<Long>> response =
                    post(
                            ApiV2Paths.ProductGroups.BASE,
                            request,
                            new ParameterizedTypeReference<ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("상품그룹 수정 (V2)")
    class UpdateProductGroupTest {

        @Test
        @DisplayName("APRD-V2-008: 유효한 요청으로 상품그룹을 수정하면 성공한다")
        void updateProductGroup_withValidRequest_success() {
            // given
            UpdateProductGroupV2ApiRequest request = createUpdateProductGroupV2Request();

            doNothing()
                    .when(updateFullProductUseCase)
                    .updateFullProduct(any(UpdateFullProductCommand.class));

            String url = ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("APRD-V2-009: 존재하지 않는 상품그룹 수정 시 404를 반환한다")
        void updateProductGroup_withNonExistentId_returns404() {
            // given
            UpdateProductGroupV2ApiRequest request = createUpdateProductGroupV2Request();

            doThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .when(updateFullProductUseCase)
                    .updateFullProduct(any(UpdateFullProductCommand.class));

            String url = ApiV2Paths.ProductGroups.BASE + "/" + NON_EXISTENT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("상품그룹 상태 변경 (V2)")
    class UpdateProductGroupStatusTest {

        @Test
        @DisplayName("APRD-V2-010: 상태를 ACTIVE로 변경하면 성공한다")
        void updateStatus_toActive_success() {
            // given
            UpdateProductGroupStatusV2ApiRequest request = createUpdateStatusV2Request("ACTIVE");

            doNothing()
                    .when(updateProductGroupStatusUseCase)
                    .execute(any(UpdateProductGroupStatusCommand.class));

            String url =
                    ApiV2Paths.ProductGroups.BASE
                            + "/"
                            + DEFAULT_PRODUCT_GROUP_ID
                            + "/status?sellerId="
                            + DEFAULT_SELLER_ID;

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    patch(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("APRD-V2-011: 상태를 INACTIVE로 변경하면 성공한다")
        void updateStatus_toInactive_success() {
            // given
            UpdateProductGroupStatusV2ApiRequest request = createUpdateStatusV2Request("INACTIVE");

            doNothing()
                    .when(updateProductGroupStatusUseCase)
                    .execute(any(UpdateProductGroupStatusCommand.class));

            String url =
                    ApiV2Paths.ProductGroups.BASE
                            + "/"
                            + DEFAULT_PRODUCT_GROUP_ID
                            + "/status?sellerId="
                            + DEFAULT_SELLER_ID;

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    patch(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("APRD-V2-012: 존재하지 않는 상품그룹 상태 변경 시 404를 반환한다")
        void updateStatus_withNonExistentId_returns404() {
            // given
            UpdateProductGroupStatusV2ApiRequest request = createUpdateStatusV2Request("ACTIVE");

            doThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .when(updateProductGroupStatusUseCase)
                    .execute(any(UpdateProductGroupStatusCommand.class));

            String url =
                    ApiV2Paths.ProductGroups.BASE
                            + "/"
                            + NON_EXISTENT_PRODUCT_GROUP_ID
                            + "/status?sellerId="
                            + DEFAULT_SELLER_ID;

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    patch(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // ProductDescription Tests
    // ============================================================

    @Nested
    @DisplayName("상품설명 조회 (V2)")
    class GetProductDescriptionTest {

        @Test
        @DisplayName("APRD-V2-013: 유효한 상품그룹 ID로 설명을 조회하면 성공한다")
        void getProductDescription_withValidId_success() {
            // given
            ProductDescriptionResponse descriptionResponse = createProductDescriptionResponse();

            given(
                            getProductDescriptionUseCase.findByProductGroupId(
                                    ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID)))
                    .willReturn(Optional.of(descriptionResponse));

            String url =
                    ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID + "/description";

            // when
            ResponseEntity<ApiResponse<ProductDescriptionV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductDescriptionV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("APRD-V2-014: 존재하지 않는 상품설명 조회 시 400을 반환한다")
        void getProductDescription_withNonExistentId_returns400() {
            // given - 컨트롤러가 IllegalArgumentException을 던지면 400 반환
            given(
                            getProductDescriptionUseCase.findByProductGroupId(
                                    ProductGroupId.of(NON_EXISTENT_PRODUCT_GROUP_ID)))
                    .willReturn(Optional.empty());

            String url =
                    ApiV2Paths.ProductGroups.BASE
                            + "/"
                            + NON_EXISTENT_PRODUCT_GROUP_ID
                            + "/description";

            // when
            ResponseEntity<ApiResponse<ProductDescriptionV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductDescriptionV2ApiResponse>>() {});

            // then - 컨트롤러가 IllegalArgumentException 발생 시 400 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("상품설명 수정 (V2)")
    class UpdateProductDescriptionTest {

        @Test
        @DisplayName("APRD-V2-015: 유효한 요청으로 상품설명을 수정하면 성공한다")
        void updateProductDescription_withValidRequest_success() {
            // given
            ProductDescriptionResponse existingDescription = createProductDescriptionResponse();
            UpdateProductDescriptionV2ApiRequest request = createUpdateDescriptionV2Request();

            given(
                            getProductDescriptionUseCase.findByProductGroupId(
                                    ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID)))
                    .willReturn(Optional.of(existingDescription));
            doNothing()
                    .when(updateProductDescriptionUseCase)
                    .execute(any(UpdateProductDescriptionCommand.class));

            String url =
                    ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID + "/description";

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("APRD-V2-016: 존재하지 않는 상품설명 수정 시 404를 반환한다")
        void updateProductDescription_withNonExistentId_returns404() {
            // given - UpdateUseCase가 NotFoundException을 던지면 404 반환
            UpdateProductDescriptionV2ApiRequest request = createUpdateDescriptionV2Request();

            doThrow(
                            new com.ryuqq.setof.domain.productdescription.exception
                                    .ProductDescriptionNotFoundException(
                                    com.ryuqq.setof.domain.productdescription.vo
                                            .ProductDescriptionId.of(
                                            NON_EXISTENT_PRODUCT_GROUP_ID)))
                    .when(updateProductDescriptionUseCase)
                    .execute(any(UpdateProductDescriptionCommand.class));

            String url =
                    ApiV2Paths.ProductGroups.BASE
                            + "/"
                            + NON_EXISTENT_PRODUCT_GROUP_ID
                            + "/description";

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then - NotFoundException은 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // ProductImage Tests
    // ============================================================

    @Nested
    @DisplayName("상품이미지 조회 (V2)")
    class GetProductImagesTest {

        @Test
        @DisplayName("APRD-V2-017: 유효한 상품그룹 ID로 이미지 목록을 조회하면 성공한다")
        void getProductImages_withValidId_success() {
            // given
            List<ProductImageResponse> imageResponses = createProductImageResponses(3);

            given(
                            getProductImageUseCase.getByProductGroupId(
                                    ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID)))
                    .willReturn(imageResponses);

            String url = ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID + "/images";

            // when
            ResponseEntity<ApiResponse<ProductImageListV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductImageListV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().items()).hasSize(3);
        }

        @Test
        @DisplayName("APRD-V2-018: 이미지가 없는 상품그룹 조회 시 빈 목록을 반환한다")
        void getProductImages_withNoImages_returnsEmptyList() {
            // given
            given(
                            getProductImageUseCase.getByProductGroupId(
                                    ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID)))
                    .willReturn(List.of());

            String url = ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID + "/images";

            // when
            ResponseEntity<ApiResponse<ProductImageListV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductImageListV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().items()).isEmpty();
        }
    }

    @Nested
    @DisplayName("상품이미지 수정 (V2)")
    class UpdateProductImageTest {

        @Test
        @DisplayName("APRD-V2-019: 유효한 요청으로 단일 이미지를 수정하면 성공한다")
        void updateProductImage_withValidRequest_success() {
            // given
            List<ProductImageResponse> existingImages = createProductImageResponses(1);
            UpdateProductImageV2ApiRequest request = createUpdateImageV2Request();

            given(
                            getProductImageUseCase.getByProductGroupId(
                                    ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID)))
                    .willReturn(existingImages);
            doNothing()
                    .when(updateProductImageUseCase)
                    .update(any(UpdateProductImageCommand.class));

            String url =
                    ApiV2Paths.ProductGroups.BASE
                            + "/"
                            + DEFAULT_PRODUCT_GROUP_ID
                            + "/images/"
                            + existingImages.get(0).id();

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }
    }

    // ============================================================
    // ProductNotice Tests
    // ============================================================

    @Nested
    @DisplayName("상품고시 조회 (V2)")
    class GetProductNoticeTest {

        @Test
        @DisplayName("APRD-V2-020: 유효한 상품그룹 ID로 고시를 조회하면 성공한다")
        void getProductNotice_withValidId_success() {
            // given
            ProductNoticeResponse noticeResponse = createProductNoticeResponse();

            given(
                            getProductNoticeUseCase.findByProductGroupId(
                                    ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID)))
                    .willReturn(Optional.of(noticeResponse));

            String url = ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID + "/notice";

            // when
            ResponseEntity<ApiResponse<ProductNoticeV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductNoticeV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("APRD-V2-021: 존재하지 않는 상품고시 조회 시 400을 반환한다")
        void getProductNotice_withNonExistentId_returns400() {
            // given - 컨트롤러가 IllegalArgumentException을 던지면 400 반환
            given(
                            getProductNoticeUseCase.findByProductGroupId(
                                    ProductGroupId.of(NON_EXISTENT_PRODUCT_GROUP_ID)))
                    .willReturn(Optional.empty());

            String url =
                    ApiV2Paths.ProductGroups.BASE + "/" + NON_EXISTENT_PRODUCT_GROUP_ID + "/notice";

            // when
            ResponseEntity<ApiResponse<ProductNoticeV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductNoticeV2ApiResponse>>() {});

            // then - 컨트롤러가 IllegalArgumentException 발생 시 400 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("상품고시 수정 (V2)")
    class UpdateProductNoticeTest {

        @Test
        @DisplayName("APRD-V2-022: 유효한 요청으로 고시를 수정하면 성공한다")
        void updateProductNotice_withValidRequest_success() {
            // given
            ProductNoticeResponse existingNotice = createProductNoticeResponse();
            UpdateProductNoticeV2ApiRequest request = createUpdateNoticeV2Request();

            given(
                            getProductNoticeUseCase.findByProductGroupId(
                                    ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID)))
                    .willReturn(Optional.of(existingNotice));
            doNothing()
                    .when(updateProductNoticeUseCase)
                    .execute(any(UpdateProductNoticeCommand.class));

            String url = ApiV2Paths.ProductGroups.BASE + "/" + DEFAULT_PRODUCT_GROUP_ID + "/notice";

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("APRD-V2-023: 존재하지 않는 상품고시 수정 시 404를 반환한다")
        void updateProductNotice_withNonExistentId_returns404() {
            // given - UpdateUseCase가 NotFoundException을 던지면 404 반환
            UpdateProductNoticeV2ApiRequest request = createUpdateNoticeV2Request();

            doThrow(
                            new com.ryuqq.setof.domain.productnotice.exception
                                    .ProductNoticeNotFoundException(
                                    com.ryuqq.setof.domain.productnotice.vo.ProductNoticeId.of(
                                            NON_EXISTENT_PRODUCT_GROUP_ID)))
                    .when(updateProductNoticeUseCase)
                    .execute(any(UpdateProductNoticeCommand.class));

            String url =
                    ApiV2Paths.ProductGroups.BASE + "/" + NON_EXISTENT_PRODUCT_GROUP_ID + "/notice";

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});

            // then - NotFoundException은 404 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // ProductStock Tests
    // ============================================================

    @Nested
    @DisplayName("단일 상품 재고 조회 (V2)")
    class GetProductStockTest {

        @Test
        @DisplayName("APRD-V2-024: 상품 ID로 재고를 조회하면 성공한다")
        void getProductStock_withValidProductId_success() {
            // given
            ProductStockResponse stockResponse = createProductStockResponse();

            given(getProductStockUseCase.execute(DEFAULT_PRODUCT_ID)).willReturn(stockResponse);

            // URL: /api/admin/v2/products/{productId}/stock
            String url = ApiV2Paths.ProductStocks.BASE + "/" + DEFAULT_PRODUCT_ID + "/stock";

            // when
            ResponseEntity<ApiResponse<ProductStockV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductStockV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().productId()).isEqualTo(DEFAULT_PRODUCT_ID);
        }

        @Test
        @DisplayName("APRD-V2-025: 존재하지 않는 상품 ID로 조회 시 404를 반환한다")
        void getProductStock_withNonExistentProductId_returns404() {
            // given
            given(getProductStockUseCase.execute(NON_EXISTENT_PRODUCT_ID))
                    .willThrow(
                            new com.ryuqq.setof.domain.productstock.exception
                                    .ProductStockNotFoundException(NON_EXISTENT_PRODUCT_ID));

            // URL: /api/admin/v2/products/{productId}/stock
            String url = ApiV2Paths.ProductStocks.BASE + "/" + NON_EXISTENT_PRODUCT_ID + "/stock";

            // when
            ResponseEntity<ApiResponse<ProductStockV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<ProductStockV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    /** ProductGroupResponse Mock 데이터를 생성합니다. (12 params) */
    private ProductGroupResponse createProductGroupResponse() {
        return new ProductGroupResponse(
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STATUS,
                null,
                null,
                List.of(createProductResponse()));
    }
}

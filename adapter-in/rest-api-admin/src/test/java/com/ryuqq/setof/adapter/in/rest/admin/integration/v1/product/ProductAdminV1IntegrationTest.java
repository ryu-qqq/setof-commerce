package com.ryuqq.setof.adapter.in.rest.admin.integration.v1.product;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.ProductAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1PageResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreatePriceV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.DeleteProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateCategoryV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.CreateProductGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupFetchV1ApiResponse;
import com.ryuqq.setof.application.product.dto.command.DeleteProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductDisplayCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.port.in.command.DeleteProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.command.MarkProductOutOfStockUseCase;
import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductDisplayUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupStatusUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductPriceUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import com.ryuqq.setof.domain.product.exception.ProductGroupNotFoundException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Product Admin V1 API 통합 테스트 (Legacy)
 *
 * <p>레거시 V1 Product Admin API의 통합 테스트를 수행합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>Query API: 상품 그룹 단건 조회, 목록 조회
 *   <li>Command API: 상품 등록, 가격 수정, 카테고리 수정, 전시 여부 수정, 품절 처리, 삭제
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API 통합 테스트로 마이그레이션 권장
 */
@DisplayName("Admin Product V1 API 통합 테스트 (Legacy)")
class ProductAdminV1IntegrationTest extends ApiIntegrationTestSupport {

    private static final String V1_PRODUCT_BASE_URL = "/api/v1/product";
    private static final String V1_PRODUCTS_BASE_URL = "/api/v1/products";

    // ============================================================
    // Query UseCases
    // ============================================================

    @Autowired private GetFullProductUseCase getFullProductUseCase;

    @Autowired private GetProductGroupsUseCase getProductGroupsUseCase;

    // ============================================================
    // Command UseCases
    // ============================================================

    @Autowired private RegisterFullProductUseCase registerFullProductUseCase;

    @Autowired private UpdateProductPriceUseCase updateProductPriceUseCase;

    @Autowired private UpdateProductGroupUseCase updateProductGroupUseCase;

    @Autowired private UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase;

    @Autowired private UpdateProductDisplayUseCase updateProductDisplayUseCase;

    @Autowired private MarkProductOutOfStockUseCase markProductOutOfStockUseCase;

    @Autowired private DeleteProductGroupUseCase deleteProductGroupUseCase;

    // ============================================================
    // Query Tests
    // ============================================================

    @Nested
    @DisplayName("상품 그룹 단건 조회 (V1)")
    class FetchProductGroupTest {

        @Test
        @DisplayName("APRD-V1-001: 유효한 상품그룹 ID로 조회하면 성공한다")
        void fetchProductGroup_withValidId_success() {
            // given
            FullProductResponse fullProductResponse = createFullProductResponse();
            given(getFullProductUseCase.getFullProduct(DEFAULT_PRODUCT_GROUP_ID))
                    .willReturn(fullProductResponse);

            String url = V1_PRODUCT_BASE_URL + "/group/" + DEFAULT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<V1ApiResponse<ProductGroupFetchV1ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<ProductGroupFetchV1ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isNotNull();
        }

        @Test
        @DisplayName("APRD-V1-002: 존재하지 않는 상품그룹 ID로 조회하면 404를 반환한다")
        void fetchProductGroup_withNonExistentId_returns404() {
            // given
            given(getFullProductUseCase.getFullProduct(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .willThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID));

            String url = V1_PRODUCT_BASE_URL + "/group/" + NON_EXISTENT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<V1ApiResponse<ProductGroupFetchV1ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<ProductGroupFetchV1ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("상품 그룹 목록 조회 (V1)")
    class FetchProductGroupsTest {

        private static final String PRODUCTS_GROUP_URL = "/api/v1/products/group";

        @Test
        @DisplayName("APRD-V1-003: 필터 없이 목록을 조회하면 성공한다")
        void fetchProductGroups_withoutFilters_success() {
            // given
            List<ProductGroupSummaryResponse> summaries = createProductGroupSummaryResponses(3);

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(summaries);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class))).willReturn(3L);

            // when
            ResponseEntity<V1ApiResponse<V1PageResponse<ProductGroupDetailV1ApiResponse>>>
                    response =
                            get(
                                    PRODUCTS_GROUP_URL,
                                    new ParameterizedTypeReference<
                                            V1ApiResponse<
                                                    V1PageResponse<
                                                            ProductGroupDetailV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data().content()).hasSize(3);
        }

        @Test
        @DisplayName("APRD-V1-004: sellerId로 필터링하면 해당 판매자의 상품만 반환된다")
        void fetchProductGroups_withSellerIdFilter_success() {
            // given
            List<ProductGroupSummaryResponse> summaries =
                    List.of(createProductGroupSummaryResponse());

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(summaries);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class))).willReturn(1L);

            String url = PRODUCTS_GROUP_URL + "?sellerId=" + DEFAULT_SELLER_ID;

            // when
            ResponseEntity<V1ApiResponse<V1PageResponse<ProductGroupDetailV1ApiResponse>>>
                    response =
                            get(
                                    url,
                                    new ParameterizedTypeReference<
                                            V1ApiResponse<
                                                    V1PageResponse<
                                                            ProductGroupDetailV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(1);
        }

        @Test
        @DisplayName("APRD-V1-005: 페이지네이션 파라미터가 적용된다")
        void fetchProductGroups_withPagination_success() {
            // given
            List<ProductGroupSummaryResponse> summaries = createProductGroupSummaryResponses(10);

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(summaries);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class)))
                    .willReturn(100L);

            String url = PRODUCTS_GROUP_URL + "?page=0&size=10";

            // when
            ResponseEntity<V1ApiResponse<V1PageResponse<ProductGroupDetailV1ApiResponse>>>
                    response =
                            get(
                                    url,
                                    new ParameterizedTypeReference<
                                            V1ApiResponse<
                                                    V1PageResponse<
                                                            ProductGroupDetailV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(10);
            assertThat(response.getBody().data().totalElements()).isEqualTo(100L);
        }

        @Test
        @DisplayName("APRD-V1-006: 검색어로 상품명을 검색할 수 있다")
        void fetchProductGroups_withSearchKeyword_success() {
            // given
            List<ProductGroupSummaryResponse> summaries =
                    List.of(createProductGroupSummaryResponse());

            given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                    .willReturn(summaries);
            given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class))).willReturn(1L);

            String url = PRODUCTS_GROUP_URL + "?keyword=" + DEFAULT_PRODUCT_GROUP_NAME;

            // when
            ResponseEntity<V1ApiResponse<V1PageResponse<ProductGroupDetailV1ApiResponse>>>
                    response =
                            get(
                                    url,
                                    new ParameterizedTypeReference<
                                            V1ApiResponse<
                                                    V1PageResponse<
                                                            ProductGroupDetailV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(1);
        }
    }

    // ============================================================
    // Command Tests - 등록
    // ============================================================

    @Nested
    @DisplayName("상품 등록 (V1)")
    class RegisterProductTest {

        @Test
        @DisplayName("APRD-V1-007: 유효한 요청으로 상품을 등록하면 성공한다")
        void registerProduct_withValidRequest_success() {
            // given
            CreateProductGroupV1ApiRequest request = createProductGroupV1Request();

            given(
                            registerFullProductUseCase.registerFullProduct(
                                    any(RegisterFullProductCommand.class)))
                    .willReturn(DEFAULT_PRODUCT_GROUP_ID);

            String url = V1_PRODUCT_BASE_URL + "/group";

            // when
            ResponseEntity<V1ApiResponse<CreateProductGroupV1ApiResponse>> response =
                    post(
                            url,
                            request,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<CreateProductGroupV1ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data().productGroupId())
                    .isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V1-008: 필수 필드 누락 시 400을 반환한다")
        void registerProduct_withMissingRequiredFields_returns400() {
            // given - sellerId가 @NotNull이므로 null 전달 시 validation 에러 발생
            CreateProductGroupV1ApiRequest request =
                    new CreateProductGroupV1ApiRequest(
                            null, // productGroupId
                            DEFAULT_PRODUCT_GROUP_NAME,
                            null, // sellerId (@NotNull 위반)
                            DEFAULT_OPTION_TYPE,
                            DEFAULT_MANAGEMENT_TYPE,
                            DEFAULT_CATEGORY_ID,
                            DEFAULT_BRAND_ID,
                            createProductStatusV1Request(),
                            createPriceV1Request(),
                            createProductNoticeV1Request(),
                            null,
                            null,
                            null,
                            List.of(createProductImageV1Request()),
                            DEFAULT_DETAIL_DESCRIPTION,
                            List.of(createOptionV1Request()));

            String url = V1_PRODUCT_BASE_URL + "/group";

            // when
            ResponseEntity<V1ApiResponse<CreateProductGroupV1ApiResponse>> response =
                    post(
                            url,
                            request,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<CreateProductGroupV1ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("APRD-V1-009: 여러 상품을 일괄 등록하면 성공한다")
        void registerProducts_withMultipleProducts_success() {
            // given
            List<CreateProductGroupV1ApiRequest> requests =
                    List.of(
                            createProductGroupV1Request(DEFAULT_SELLER_ID, "상품1"),
                            createProductGroupV1Request(DEFAULT_SELLER_ID, "상품2"),
                            createProductGroupV1Request(DEFAULT_SELLER_ID, "상품3"));

            given(
                            registerFullProductUseCase.registerFullProduct(
                                    any(RegisterFullProductCommand.class)))
                    .willReturn(1L, 2L, 3L);

            String url = V1_PRODUCTS_BASE_URL + "/group";

            // when
            ResponseEntity<V1ApiResponse<List<Long>>> response =
                    post(
                            url,
                            requests,
                            new ParameterizedTypeReference<V1ApiResponse<List<Long>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(3);
        }
    }

    // ============================================================
    // Command Tests - 가격 수정
    // ============================================================

    @Nested
    @DisplayName("가격 수정 (V1)")
    class UpdatePriceTest {

        @Test
        @DisplayName("APRD-V1-010: 유효한 요청으로 가격을 수정하면 성공한다")
        void updatePrice_withValidRequest_success() {
            // given
            CreatePriceV1ApiRequest request = createUpdatedPriceV1Request();

            doNothing()
                    .when(updateProductPriceUseCase)
                    .execute(any(UpdateProductPriceCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/group/" + DEFAULT_PRODUCT_GROUP_ID + "/price";

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    patch(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V1-011: 존재하지 않는 상품그룹의 가격 수정 시 404를 반환한다")
        void updatePrice_withNonExistentProductGroup_returns404() {
            // given
            CreatePriceV1ApiRequest request = createUpdatedPriceV1Request();

            doThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .when(updateProductPriceUseCase)
                    .execute(any(UpdateProductPriceCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/group/" + NON_EXISTENT_PRODUCT_GROUP_ID + "/price";

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    patch(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // Command Tests - 카테고리 수정
    // ============================================================

    @Nested
    @DisplayName("카테고리 수정 (V1)")
    class UpdateCategoryTest {

        @Test
        @DisplayName("APRD-V1-012: 유효한 요청으로 카테고리를 수정하면 성공한다")
        void updateCategory_withValidRequest_success() {
            // given
            Long newCategoryId = 20L;
            UpdateCategoryV1ApiRequest request = createUpdateCategoryV1Request(newCategoryId);

            doNothing()
                    .when(updateProductGroupUseCase)
                    .execute(any(UpdateProductGroupCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/group/" + DEFAULT_PRODUCT_GROUP_ID + "/category";

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    patch(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V1-013: 존재하지 않는 상품그룹의 카테고리 수정 시 404를 반환한다")
        void updateCategory_withNonExistentProductGroup_returns404() {
            // given
            UpdateCategoryV1ApiRequest request = createUpdateCategoryV1Request(DEFAULT_CATEGORY_ID);

            doThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .when(updateProductGroupUseCase)
                    .execute(any(UpdateProductGroupCommand.class));

            String url =
                    V1_PRODUCT_BASE_URL + "/group/" + NON_EXISTENT_PRODUCT_GROUP_ID + "/category";

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    patch(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // Command Tests - 전시 여부 수정
    // ============================================================

    @Nested
    @DisplayName("전시 여부 수정 (V1)")
    class UpdateDisplayYnTest {

        @Test
        @DisplayName("APRD-V1-014: 상품그룹 전시 상태를 Y로 변경하면 ACTIVE 상태가 된다")
        void updateGroupDisplayYn_toY_success() {
            // given
            UpdateDisplayYnV1ApiRequest request = createUpdateDisplayYnV1Request("Y");

            doNothing()
                    .when(updateProductGroupStatusUseCase)
                    .execute(any(UpdateProductGroupStatusCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/group/" + DEFAULT_PRODUCT_GROUP_ID + "/display-yn";

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    patch(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V1-015: 상품그룹 전시 상태를 N으로 변경하면 INACTIVE 상태가 된다")
        void updateGroupDisplayYn_toN_success() {
            // given
            UpdateDisplayYnV1ApiRequest request = createUpdateDisplayYnV1Request("N");

            doNothing()
                    .when(updateProductGroupStatusUseCase)
                    .execute(any(UpdateProductGroupStatusCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/group/" + DEFAULT_PRODUCT_GROUP_ID + "/display-yn";

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    patch(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V1-016: 개별 상품의 전시 여부를 변경하면 성공한다")
        void updateProductDisplayYn_success() {
            // given
            UpdateDisplayYnV1ApiRequest request = createUpdateDisplayYnV1Request("Y");

            doNothing()
                    .when(updateProductDisplayUseCase)
                    .execute(any(UpdateProductDisplayCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/" + DEFAULT_PRODUCT_ID + "/display-yn";

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    patch(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_PRODUCT_ID);
        }
    }

    // ============================================================
    // Command Tests - 상품 그룹 수정
    // ============================================================

    @Nested
    @DisplayName("상품 그룹 수정 (V1)")
    class UpdateProductGroupTest {

        @Test
        @DisplayName("APRD-V1-017: 유효한 요청으로 상품 그룹을 수정하면 성공한다")
        void updateProductGroup_withValidRequest_success() {
            // given
            UpdateProductGroupV1ApiRequest request = createUpdateProductGroupV1Request();

            doNothing()
                    .when(updateProductGroupUseCase)
                    .execute(any(UpdateProductGroupCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/group/" + DEFAULT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    put(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V1-018: 존재하지 않는 상품그룹 수정 시 404를 반환한다")
        void updateProductGroup_withNonExistentId_returns404() {
            // given
            UpdateProductGroupV1ApiRequest request = createUpdateProductGroupV1Request();

            doThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .when(updateProductGroupUseCase)
                    .execute(any(UpdateProductGroupCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/group/" + NON_EXISTENT_PRODUCT_GROUP_ID;

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    put(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // Command Tests - 품절 처리
    // ============================================================

    @Nested
    @DisplayName("품절 처리 (V1)")
    class OutOfStockTest {

        @Test
        @DisplayName("APRD-V1-019: 상품그룹의 모든 상품을 품절 처리하면 성공한다")
        void outOfStock_withValidProductGroup_success() {
            // given
            FullProductResponse fullProductResponse = createFullProductResponse();

            given(getFullProductUseCase.getFullProduct(DEFAULT_PRODUCT_GROUP_ID))
                    .willReturn(fullProductResponse);
            doNothing().when(markProductOutOfStockUseCase).execute(any());

            String url = V1_PRODUCT_BASE_URL + "/group/" + DEFAULT_PRODUCT_GROUP_ID + "/out-stock";

            // when
            ResponseEntity<V1ApiResponse<Set<ProductFetchV1ApiResponse>>> response =
                    patch(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<Set<ProductFetchV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotEmpty();
        }

        @Test
        @DisplayName("APRD-V1-020: 존재하지 않는 상품그룹 품절 처리 시 404를 반환한다")
        void outOfStock_withNonExistentProductGroup_returns404() {
            // given
            given(getFullProductUseCase.getFullProduct(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .willThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID));

            String url =
                    V1_PRODUCT_BASE_URL + "/group/" + NON_EXISTENT_PRODUCT_GROUP_ID + "/out-stock";

            // when
            ResponseEntity<V1ApiResponse<Set<ProductFetchV1ApiResponse>>> response =
                    patch(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<Set<ProductFetchV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // Command Tests - 삭제
    // ============================================================

    @Nested
    @DisplayName("상품 그룹 삭제 (V1)")
    class DeleteProductGroupTest {

        @Test
        @DisplayName("APRD-V1-021: 유효한 요청으로 상품 그룹을 삭제하면 성공한다")
        void deleteProductGroup_withValidRequest_success() {
            // given
            List<Long> productGroupIds = List.of(DEFAULT_PRODUCT_GROUP_ID);
            DeleteProductGroupV1ApiRequest request =
                    createDeleteProductGroupV1Request(productGroupIds);

            doNothing()
                    .when(deleteProductGroupUseCase)
                    .execute(any(DeleteProductGroupCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/groups";

            // when
            ResponseEntity<V1ApiResponse<List<Long>>> response =
                    deleteWithBody(
                            url,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<List<Long>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).containsExactly(DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("APRD-V1-022: 여러 상품 그룹을 일괄 삭제하면 성공한다")
        void deleteProductGroups_withMultipleIds_success() {
            // given
            List<Long> productGroupIds = List.of(1L, 2L, 3L);
            DeleteProductGroupV1ApiRequest request =
                    createDeleteProductGroupV1Request(productGroupIds);

            doNothing()
                    .when(deleteProductGroupUseCase)
                    .execute(any(DeleteProductGroupCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/groups";

            // when
            ResponseEntity<V1ApiResponse<List<Long>>> response =
                    deleteWithBody(
                            url,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<List<Long>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).containsExactly(1L, 2L, 3L);
        }

        @Test
        @DisplayName("APRD-V1-023: 존재하지 않는 상품그룹 삭제 시 404를 반환한다")
        void deleteProductGroup_withNonExistentId_returns404() {
            // given
            List<Long> productGroupIds = List.of(NON_EXISTENT_PRODUCT_GROUP_ID);
            DeleteProductGroupV1ApiRequest request =
                    createDeleteProductGroupV1Request(productGroupIds);

            doThrow(new ProductGroupNotFoundException(NON_EXISTENT_PRODUCT_GROUP_ID))
                    .when(deleteProductGroupUseCase)
                    .execute(any(DeleteProductGroupCommand.class));

            String url = V1_PRODUCT_BASE_URL + "/groups";

            // when
            ResponseEntity<V1ApiResponse<List<Long>>> response =
                    deleteWithBody(
                            url,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<List<Long>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}

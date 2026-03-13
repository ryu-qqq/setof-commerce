package com.ryuqq.setof.integration.test.e2e.admin.productgroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductOptionMappingJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionValueJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository.ProductGroupDescriptionJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.repository.ProductGroupImageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeEntryJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.RefundPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository.RefundPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.SellerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.ShippingPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository.ShippingPolicyJpaRepository;
import com.ryuqq.setof.integration.test.common.base.AdminE2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * ProductGroup Admin E2E 통합 테스트.
 *
 * <p>상품그룹 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.PRODUCT)
@DisplayName("상품그룹 Admin API E2E 테스트")
class ProductGroupAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/product-groups";

    @Autowired private ProductNoticeEntryJpaRepository productNoticeEntryJpaRepository;
    @Autowired private ProductNoticeJpaRepository productNoticeJpaRepository;
    @Autowired private ProductGroupDescriptionJpaRepository productGroupDescriptionJpaRepository;
    @Autowired private ProductOptionMappingJpaRepository productOptionMappingJpaRepository;
    @Autowired private ProductJpaRepository productJpaRepository;
    @Autowired private SellerOptionValueJpaRepository sellerOptionValueJpaRepository;
    @Autowired private SellerOptionGroupJpaRepository sellerOptionGroupJpaRepository;
    @Autowired private ProductGroupImageJpaRepository productGroupImageJpaRepository;
    @Autowired private ProductGroupJpaRepository productGroupJpaRepository;
    @Autowired private ShippingPolicyJpaRepository shippingPolicyJpaRepository;
    @Autowired private RefundPolicyJpaRepository refundPolicyJpaRepository;
    @Autowired private BrandJpaRepository brandJpaRepository;
    @Autowired private CategoryJpaRepository categoryJpaRepository;
    @Autowired private SellerJpaRepository sellerJpaRepository;

    private Long savedSellerId;
    private Long savedBrandId;
    private Long savedCategoryId;
    private Long savedShippingPolicyId;
    private Long savedRefundPolicyId;

    @BeforeEach
    void setUp() {
        // 외래키 제약 순서에 맞게 역순으로 삭제
        productNoticeEntryJpaRepository.deleteAll();
        productNoticeJpaRepository.deleteAll();
        productGroupDescriptionJpaRepository.deleteAll();
        productOptionMappingJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
        sellerOptionValueJpaRepository.deleteAll();
        sellerOptionGroupJpaRepository.deleteAll();
        productGroupImageJpaRepository.deleteAll();
        productGroupJpaRepository.deleteAll();
        shippingPolicyJpaRepository.deleteAll();
        refundPolicyJpaRepository.deleteAll();
        brandJpaRepository.deleteAll();
        categoryJpaRepository.deleteAll();
        sellerJpaRepository.deleteAll();

        // 선행 데이터 생성
        SellerJpaEntity seller = sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
        savedSellerId = seller.getId();

        BrandJpaEntity brand = brandJpaRepository.save(BrandJpaEntityFixtures.newEntity());
        savedBrandId = brand.getId();

        CategoryJpaEntity category =
                categoryJpaRepository.save(CategoryJpaEntityFixtures.newEntity());
        savedCategoryId = category.getId();

        ShippingPolicyJpaEntity shippingPolicy =
                shippingPolicyJpaRepository.save(ShippingPolicyJpaEntityFixtures.newEntity());
        savedShippingPolicyId = shippingPolicy.getId();

        RefundPolicyJpaEntity refundPolicy =
                refundPolicyJpaRepository.save(RefundPolicyJpaEntityFixtures.newEntity());
        savedRefundPolicyId = refundPolicy.getId();
    }

    @Nested
    @DisplayName("POST /v2/admin/product-groups - 상품그룹 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 요청으로 상품그룹 등록 성공")
        void shouldRegisterProductGroupSuccessfully() {
            // given
            Map<String, Object> request = createRegisterRequest();

            // when
            Response response = givenAdmin().body(request).when().post(BASE_PATH);

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.productGroupId", greaterThan(0));

            Long createdId = response.jsonPath().getLong("data.productGroupId");
            assertThat(productGroupJpaRepository.findById(createdId)).isPresent();

            ProductGroupJpaEntity saved =
                    productGroupJpaRepository.findById(createdId).orElseThrow();
            assertThat(saved.getProductGroupName()).isEqualTo("E2E 테스트 상품그룹");
            assertThat(saved.getSellerId()).isEqualTo(savedSellerId);
        }

        @Test
        @DisplayName("productGroupName 누락시 400 에러 반환")
        void shouldReturn400WhenProductGroupNameMissing() {
            // given - productGroupName 누락
            Map<String, Object> request = createRegisterRequest();
            request.put("productGroupName", null);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("GET /v2/admin/product-groups/{id} - 상품그룹 단건 조회")
    class GetDetailTest {

        @Test
        @DisplayName("등록 후 ID로 상품그룹 상세 조회 성공")
        void shouldGetProductGroupDetailSuccessfully() {
            // given - 먼저 상품그룹 등록
            Map<String, Object> request = createRegisterRequest();
            Response registerResponse = givenAdmin().body(request).when().post(BASE_PATH);
            registerResponse.then().statusCode(HttpStatus.CREATED.value());
            Long productGroupId = registerResponse.jsonPath().getLong("data.productGroupId");

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/" + productGroupId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", notNullValue())
                    .body("data.productGroupName", notNullValue());
        }
    }

    @Nested
    @DisplayName("PATCH /v2/admin/product-groups/{id}/basic-info - 상품그룹 기본정보 수정")
    class UpdateBasicInfoTest {

        @Test
        @DisplayName("상품그룹 기본정보 수정 성공")
        void shouldUpdateBasicInfoSuccessfully() {
            // given - 먼저 상품그룹 등록
            Map<String, Object> registerRequest = createRegisterRequest();
            Response registerResponse = givenAdmin().body(registerRequest).when().post(BASE_PATH);
            registerResponse.then().statusCode(HttpStatus.CREATED.value());
            Long productGroupId = registerResponse.jsonPath().getLong("data.productGroupId");

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("productGroupName", "수정된 상품그룹명");
            updateRequest.put("brandId", savedBrandId);
            updateRequest.put("categoryId", savedCategoryId);
            updateRequest.put("shippingPolicyId", savedShippingPolicyId);
            updateRequest.put("refundPolicyId", savedRefundPolicyId);

            // when & then
            givenAdmin()
                    .body(updateRequest)
                    .when()
                    .patch(BASE_PATH + "/" + productGroupId + "/basic-info")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB에서 변경 확인
            ProductGroupJpaEntity updated =
                    productGroupJpaRepository.findById(productGroupId).orElseThrow();
            assertThat(updated.getProductGroupName()).isEqualTo("수정된 상품그룹명");
        }

        @Test
        @DisplayName("존재하지 않는 상품그룹 ID로 기본정보 수정 시 404 반환")
        void shouldReturn404WhenProductGroupNotFoundOnBasicInfoUpdate() {
            // given
            Long nonExistentId = 999999L;
            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("productGroupName", "수정된 상품그룹명");
            updateRequest.put("brandId", savedBrandId);
            updateRequest.put("categoryId", savedCategoryId);
            updateRequest.put("shippingPolicyId", savedShippingPolicyId);
            updateRequest.put("refundPolicyId", savedRefundPolicyId);

            // when & then
            givenAdmin()
                    .body(updateRequest)
                    .when()
                    .patch(BASE_PATH + "/" + nonExistentId + "/basic-info")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("PUT /v2/admin/product-groups/{id} - 상품그룹 전체 수정")
    class UpdateFullTest {

        @Test
        @DisplayName("유효한 요청으로 상품그룹 전체 수정 성공")
        void shouldUpdateProductGroupFullSuccessfully() {
            // given - 먼저 상품그룹 등록
            Map<String, Object> registerRequest = createRegisterRequest();
            Response registerResponse = givenAdmin().body(registerRequest).when().post(BASE_PATH);
            registerResponse.then().statusCode(HttpStatus.CREATED.value());
            Long productGroupId = registerResponse.jsonPath().getLong("data.productGroupId");

            Map<String, Object> updateRequest = createFullUpdateRequest("전체 수정된 상품그룹명");

            // when
            givenAdmin()
                    .body(updateRequest)
                    .when()
                    .put(BASE_PATH + "/" + productGroupId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB에서 기본 정보 변경 확인
            ProductGroupJpaEntity updated =
                    productGroupJpaRepository.findById(productGroupId).orElseThrow();
            assertThat(updated.getProductGroupName()).isEqualTo("전체 수정된 상품그룹명");
        }

        @Test
        @DisplayName("전체 수정 후 이미지가 교체되었는지 확인")
        void shouldReplaceImagesOnFullUpdate() {
            // given
            Map<String, Object> registerRequest = createRegisterRequest();
            Response registerResponse = givenAdmin().body(registerRequest).when().post(BASE_PATH);
            registerResponse.then().statusCode(HttpStatus.CREATED.value());
            Long productGroupId = registerResponse.jsonPath().getLong("data.productGroupId");

            Map<String, Object> updateRequest = createFullUpdateRequest("이미지 교체 테스트 상품그룹");

            // when
            givenAdmin()
                    .body(updateRequest)
                    .when()
                    .put(BASE_PATH + "/" + productGroupId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB에서 이미지 존재 확인
            assertThat(productGroupImageJpaRepository.findAll()).isNotEmpty();
        }

        @Test
        @DisplayName("productGroupName 누락 시 전체 수정 400 에러 반환")
        void shouldReturn400WhenProductGroupNameMissingOnFullUpdate() {
            // given - 먼저 상품그룹 등록
            Map<String, Object> registerRequest = createRegisterRequest();
            Response registerResponse = givenAdmin().body(registerRequest).when().post(BASE_PATH);
            registerResponse.then().statusCode(HttpStatus.CREATED.value());
            Long productGroupId = registerResponse.jsonPath().getLong("data.productGroupId");

            Map<String, Object> updateRequest = createFullUpdateRequest(null);

            // when & then
            givenAdmin()
                    .body(updateRequest)
                    .when()
                    .put(BASE_PATH + "/" + productGroupId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("존재하지 않는 상품그룹 ID로 전체 수정 시 404 반환")
        void shouldReturn404WhenProductGroupNotFoundOnFullUpdate() {
            // given
            Long nonExistentId = 999999L;
            Map<String, Object> updateRequest = createFullUpdateRequest("수정 요청 상품그룹명");

            // when & then
            givenAdmin()
                    .body(updateRequest)
                    .when()
                    .put(BASE_PATH + "/" + nonExistentId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("GET /v2/admin/product-groups/{id} - 상품그룹 단건 조회 에러 케이스")
    class GetDetailErrorTest {

        @Test
        @DisplayName("존재하지 않는 상품그룹 ID로 조회 시 404 반환")
        void shouldReturn404WhenProductGroupNotFound() {
            // given
            Long nonExistentId = 999999L;

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/" + nonExistentId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("POST /v2/admin/product-groups - 상품그룹 등록 필드 검증")
    class RegisterValidationTest {

        @Test
        @DisplayName("sellerId 누락 시 400 에러 반환")
        void shouldReturn400WhenSellerIdMissing() {
            // given
            Map<String, Object> request = createRegisterRequest();
            request.put("sellerId", null);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("images 빈 목록 시 400 에러 반환")
        void shouldReturn400WhenImagesEmpty() {
            // given
            Map<String, Object> request = createRegisterRequest();
            request.put("images", List.of());

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("products 빈 목록 시 400 에러 반환")
        void shouldReturn400WhenProductsEmpty() {
            // given
            Map<String, Object> request = createRegisterRequest();
            request.put("products", List.of());

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("전체 플로우 시나리오")
    class FullFlowTest {

        @Test
        @DisplayName("등록 -> 상세 조회 -> 전체 수정 -> 기본정보 수정 플로우")
        void shouldCompleteRegisterAndUpdateFlow() {
            // step 1: 상품그룹 등록
            Map<String, Object> registerRequest = createRegisterRequest();
            Response registerResponse = givenAdmin().body(registerRequest).when().post(BASE_PATH);
            registerResponse.then().statusCode(HttpStatus.CREATED.value());
            Long productGroupId = registerResponse.jsonPath().getLong("data.productGroupId");
            assertThat(productGroupId).isGreaterThan(0);

            // step 2: 상세 조회 성공 확인
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/" + productGroupId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // step 3: 전체 수정
            Map<String, Object> fullUpdateRequest = createFullUpdateRequest("플로우 전체 수정명");
            givenAdmin()
                    .body(fullUpdateRequest)
                    .when()
                    .put(BASE_PATH + "/" + productGroupId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            ProductGroupJpaEntity afterFullUpdate =
                    productGroupJpaRepository.findById(productGroupId).orElseThrow();
            assertThat(afterFullUpdate.getProductGroupName()).isEqualTo("플로우 전체 수정명");

            // step 4: 기본정보 수정
            Map<String, Object> basicInfoRequest = new HashMap<>();
            basicInfoRequest.put("productGroupName", "플로우 기본정보 수정명");
            basicInfoRequest.put("brandId", savedBrandId);
            basicInfoRequest.put("categoryId", savedCategoryId);
            basicInfoRequest.put("shippingPolicyId", savedShippingPolicyId);
            basicInfoRequest.put("refundPolicyId", savedRefundPolicyId);

            givenAdmin()
                    .body(basicInfoRequest)
                    .when()
                    .patch(BASE_PATH + "/" + productGroupId + "/basic-info")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            ProductGroupJpaEntity afterBasicUpdate =
                    productGroupJpaRepository.findById(productGroupId).orElseThrow();
            assertThat(afterBasicUpdate.getProductGroupName()).isEqualTo("플로우 기본정보 수정명");
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createFullUpdateRequest(String productGroupName) {
        Map<String, Object> request = new HashMap<>();
        request.put("productGroupName", productGroupName);
        request.put("brandId", savedBrandId);
        request.put("categoryId", savedCategoryId);
        request.put("shippingPolicyId", savedShippingPolicyId);
        request.put("refundPolicyId", savedRefundPolicyId);
        request.put("optionType", "SINGLE");
        request.put("regularPrice", 60000);
        request.put("currentPrice", 55000);
        request.put(
                "images",
                List.of(
                        Map.of(
                                "imageType", "THUMBNAIL",
                                "imageUrl", "https://example.com/updated-thumb.png",
                                "sortOrder", 1)));
        Map<String, Object> productEntry = new HashMap<>();
        productEntry.put("productId", null);
        productEntry.put("skuCode", null);
        productEntry.put("regularPrice", 60000);
        productEntry.put("currentPrice", 55000);
        productEntry.put("stockQuantity", 50);
        productEntry.put("sortOrder", 0);
        productEntry.put("selectedOptions", List.of());
        request.put("products", List.of(productEntry));
        request.put(
                "description",
                Map.of("content", "<p>수정된 상품 설명</p>", "descriptionImages", List.of()));
        request.put(
                "notice",
                Map.of(
                        "entries",
                        List.of(
                                Map.of(
                                        "noticeFieldId", 1,
                                        "fieldName", "제조국",
                                        "fieldValue", "대한민국"))));
        return request;
    }

    private Map<String, Object> createRegisterRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("sellerId", savedSellerId);
        request.put("brandId", savedBrandId);
        request.put("categoryId", savedCategoryId);
        request.put("shippingPolicyId", savedShippingPolicyId);
        request.put("refundPolicyId", savedRefundPolicyId);
        request.put("productGroupName", "E2E 테스트 상품그룹");
        request.put("optionType", "SINGLE");
        request.put("regularPrice", 50000);
        request.put("currentPrice", 45000);
        request.put(
                "images",
                List.of(
                        Map.of(
                                "imageType",
                                "THUMBNAIL",
                                "imageUrl",
                                "https://example.com/thumb.png",
                                "sortOrder",
                                1),
                        Map.of(
                                "imageType",
                                "DETAIL",
                                "imageUrl",
                                "https://example.com/detail.png",
                                "sortOrder",
                                2)));
        request.put(
                "products",
                List.of(
                        Map.of(
                                "regularPrice",
                                50000,
                                "currentPrice",
                                45000,
                                "stockQuantity",
                                100,
                                "sortOrder",
                                0,
                                "selectedOptions",
                                List.of())));
        request.put(
                "description",
                Map.of("content", "<p>테스트 상품 설명</p>", "descriptionImages", List.of()));
        request.put(
                "notice",
                Map.of(
                        "entries",
                        List.of(
                                Map.of(
                                        "noticeFieldId",
                                        1,
                                        "fieldName",
                                        "제조국",
                                        "fieldValue",
                                        "대한민국"))));
        return request;
    }
}

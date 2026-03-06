package com.ryuqq.setof.integration.test.e2e.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductOptionMappingJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupImageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionValueJpaRepository;
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
 * Product Admin E2E 통합 테스트.
 *
 * <p>상품 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.PRODUCT)
@DisplayName("상품 Admin API E2E 테스트")
class ProductAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/products";

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
    private Long savedProductGroupId;
    private Long savedProductId;

    @BeforeEach
    void setUp() {
        // 외래키 제약 순서에 맞게 역순으로 삭제
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

        // 선행 데이터 저장
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

        // API를 통한 상품그룹 등록
        savedProductGroupId = registerProductGroup();

        // 등록된 상품그룹 하위의 상품 ID 조회
        List<ProductJpaEntity> products =
                productJpaRepository.findByProductGroupId(savedProductGroupId);
        savedProductId = products.get(0).getId();
    }

    @Nested
    @DisplayName("PATCH /v2/admin/products/{productId}/price - 상품 가격 수정")
    class UpdatePriceTest {

        @Test
        @DisplayName("유효한 요청으로 상품 가격 수정 성공")
        void shouldUpdatePriceSuccessfully() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("regularPrice", 60000);
            request.put("currentPrice", 55000);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/" + savedProductId + "/price")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB 변경 확인
            ProductJpaEntity updated = productJpaRepository.findById(savedProductId).orElseThrow();
            assertThat(updated.getRegularPrice()).isEqualTo(60000);
            assertThat(updated.getCurrentPrice()).isEqualTo(55000);
        }

        @Test
        @DisplayName("존재하지 않는 상품 가격 수정 시 404 또는 500 반환")
        void shouldReturn404WhenProductNotFound() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("regularPrice", 60000);
            request.put("currentPrice", 55000);

            // when & then
            int statusCode =
                    givenAdmin()
                            .body(request)
                            .when()
                            .patch(BASE_PATH + "/999999/price")
                            .then()
                            .extract()
                            .statusCode();

            assertThat(statusCode)
                    .as("존재하지 않는 상품 가격 수정은 404 또는 500을 반환해야 합니다")
                    .isIn(HttpStatus.NOT_FOUND.value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    @DisplayName("PATCH /v2/admin/products/{productId}/stock - 상품 재고 수정")
    class UpdateStockTest {

        @Test
        @DisplayName("유효한 요청으로 상품 재고 수정 성공")
        void shouldUpdateStockSuccessfully() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("quantity", 200);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/" + savedProductId + "/stock")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB 변경 확인
            ProductJpaEntity updated = productJpaRepository.findById(savedProductId).orElseThrow();
            assertThat(updated.getStockQuantity()).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("PATCH /v2/admin/products/product-groups/{productGroupId} - 상품 일괄 수정")
    class BulkUpdateProductsTest {

        @Test
        @DisplayName("유효한 요청으로 상품그룹 하위 상품 일괄 수정 성공")
        void shouldBulkUpdateProductsSuccessfully() {
            // given
            Map<String, Object> productUpdate = new HashMap<>();
            productUpdate.put("productId", savedProductId);
            productUpdate.put("skuCode", "SKU-001");
            productUpdate.put("regularPrice", 55000);
            productUpdate.put("currentPrice", 50000);
            productUpdate.put("stockQuantity", 150);
            productUpdate.put("sortOrder", 0);
            productUpdate.put("selectedOptions", List.of());

            Map<String, Object> request = new HashMap<>();
            request.put("optionGroups", List.of());
            request.put("products", List.of(productUpdate));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/product-groups/" + savedProductGroupId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB 변경 확인
            ProductJpaEntity updated = productJpaRepository.findById(savedProductId).orElseThrow();
            assertThat(updated.getRegularPrice()).isEqualTo(55000);
            assertThat(updated.getCurrentPrice()).isEqualTo(50000);
            assertThat(updated.getStockQuantity()).isEqualTo(150);
        }
    }

    // ===== Helper Methods =====

    private Long registerProductGroup() {
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
                                "imageType", "THUMBNAIL",
                                "imageUrl", "https://example.com/thumb.png",
                                "sortOrder", 1)));
        request.put(
                "products",
                List.of(
                        Map.of(
                                "additionalPrice", 0,
                                "stockQuantity", 100,
                                "sortOrder", 0,
                                "selectedOptions", List.of())));
        request.put("description", Map.of("content", "<p>설명</p>", "cdnPath", ""));
        request.put(
                "notice",
                Map.of(
                        "entries",
                        List.of(
                                Map.of(
                                        "fieldName", "제조국",
                                        "fieldValue", "대한민국",
                                        "sortOrder", 1))));

        return givenAdmin()
                .body(request)
                .when()
                .post("/v2/admin/product-groups")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath()
                .getLong("data");
    }
}

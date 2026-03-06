package com.ryuqq.setof.integration.test.e2e.admin.productimage;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductOptionMappingJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productdescription.repository.ProductGroupDescriptionJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupImageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionValueJpaRepository;
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
 * ProductImage Admin E2E 통합 테스트.
 *
 * <p>상품 이미지 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.PRODUCT)
@DisplayName("상품 이미지 Admin API E2E 테스트")
class ProductImageAdminE2ETest extends AdminE2ETestBase {

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
    @DisplayName("PUT /v2/admin/product-groups/{productGroupId}/images - 상품 이미지 전체 교체")
    class UpdateImagesTest {

        @Test
        @DisplayName("유효한 요청으로 이미지 전체 교체 성공")
        void shouldUpdateImagesSuccessfully() {
            // given - 상품그룹 등록
            Long productGroupId = registerProductGroup();

            Map<String, Object> request = new HashMap<>();
            request.put(
                    "images",
                    List.of(
                            Map.of(
                                    "imageType",
                                    "THUMBNAIL",
                                    "imageUrl",
                                    "https://example.com/new-thumb.png",
                                    "sortOrder",
                                    1),
                            Map.of(
                                    "imageType",
                                    "DETAIL",
                                    "imageUrl",
                                    "https://example.com/new-detail-1.png",
                                    "sortOrder",
                                    2),
                            Map.of(
                                    "imageType",
                                    "DETAIL",
                                    "imageUrl",
                                    "https://example.com/new-detail-2.png",
                                    "sortOrder",
                                    3)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + productGroupId + "/images")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB에서 변경 확인 - 삭제되지 않은 이미지 3개가 존재해야 함
            List<ProductGroupImageJpaEntity> images =
                    productGroupImageJpaRepository.findByProductGroupIdAndDeletedAtIsNull(
                            productGroupId);
            assertThat(images).hasSize(3);
        }

        @Test
        @DisplayName("빈 이미지 목록으로 교체 요청시 400 에러 반환")
        void shouldReturn400WhenImagesEmpty() {
            // given - 상품그룹 등록
            Long productGroupId = registerProductGroup();

            Map<String, Object> request = new HashMap<>();
            request.put("images", List.of());

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + productGroupId + "/images")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
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
                                "imageType",
                                "THUMBNAIL",
                                "imageUrl",
                                "https://example.com/initial.png",
                                "sortOrder",
                                1)));
        request.put(
                "products",
                List.of(
                        Map.of(
                                "additionalPrice",
                                0,
                                "stockQuantity",
                                100,
                                "sortOrder",
                                0,
                                "selectedOptions",
                                List.of())));
        request.put("description", Map.of("content", "<p>설명</p>", "cdnPath", ""));
        request.put(
                "notice",
                Map.of(
                        "entries",
                        List.of(Map.of("fieldName", "제조국", "fieldValue", "대한민국", "sortOrder", 1))));

        return givenAdmin()
                .body(request)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath()
                .getLong("data.productGroupId");
    }
}

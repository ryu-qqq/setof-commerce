package com.ryuqq.setof.integration.test.e2e.admin.productnotice;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductOptionMappingJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupImageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionValueJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
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
 * ProductNotice Admin E2E 통합 테스트.
 *
 * <p>상품 고시정보 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.E2E)
@Tag(TestTags.ADMIN)
@Tag(TestTags.PRODUCT)
@DisplayName("상품 고시정보 Admin API E2E 테스트")
class ProductNoticeAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/product-groups";

    @Autowired private ProductNoticeJpaRepository productNoticeJpaRepository;
    @Autowired private ProductNoticeEntryJpaRepository productNoticeEntryJpaRepository;
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

        // API를 통한 상품그룹 등록 (notice 포함)
        savedProductGroupId = registerProductGroup();
    }

    @Nested
    @DisplayName("PUT /v2/admin/product-groups/{productGroupId}/notice - 상품 고시정보 수정")
    class UpdateNoticeTest {

        @Test
        @DisplayName("유효한 요청으로 상품 고시정보 수정 성공")
        void shouldUpdateNoticeSuccessfully() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put(
                    "entries",
                    List.of(
                            Map.of("fieldName", "제조국", "fieldValue", "중국", "sortOrder", 1),
                            Map.of(
                                    "fieldName", "제조자",
                                    "fieldValue", "테스트 제조사",
                                    "sortOrder", 2),
                            Map.of(
                                    "fieldName", "수입자",
                                    "fieldValue", "테스트 수입사",
                                    "sortOrder", 3)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + savedProductGroupId + "/notice")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // DB 변경 확인 - notice 존재 여부
            ProductNoticeJpaEntity notice =
                    productNoticeJpaRepository
                            .findByProductGroupId(savedProductGroupId)
                            .orElseThrow(
                                    () ->
                                            new AssertionError(
                                                    "productGroupId="
                                                            + savedProductGroupId
                                                            + "에 해당하는 고시정보가 존재하지 않습니다."));

            // DB 변경 확인 - entry 개수 검증
            List<ProductNoticeEntryJpaEntity> entries =
                    productNoticeEntryJpaRepository.findByProductNoticeId(notice.getId());
            assertThat(entries).hasSize(3);
        }

        @Test
        @DisplayName("다른 항목으로 재수정 시 항목 수가 변경됨")
        void shouldUpdateNoticeWithDifferentEntries() {
            // given - 1차 수정: 3개 항목
            Map<String, Object> firstRequest = new HashMap<>();
            firstRequest.put(
                    "entries",
                    List.of(
                            Map.of("fieldName", "제조국", "fieldValue", "중국", "sortOrder", 1),
                            Map.of(
                                    "fieldName", "제조자",
                                    "fieldValue", "테스트 제조사",
                                    "sortOrder", 2),
                            Map.of(
                                    "fieldName", "수입자",
                                    "fieldValue", "테스트 수입사",
                                    "sortOrder", 3)));

            givenAdmin()
                    .body(firstRequest)
                    .when()
                    .put(BASE_PATH + "/" + savedProductGroupId + "/notice")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // 1차 수정 후 항목 수 검증
            ProductNoticeJpaEntity noticeAfterFirst =
                    productNoticeJpaRepository
                            .findByProductGroupId(savedProductGroupId)
                            .orElseThrow(() -> new AssertionError("1차 수정 후 고시정보가 존재하지 않습니다."));
            List<ProductNoticeEntryJpaEntity> entriesAfterFirst =
                    productNoticeEntryJpaRepository.findByProductNoticeId(noticeAfterFirst.getId());
            assertThat(entriesAfterFirst).hasSize(3);

            // given - 2차 수정: 1개 항목으로 변경
            Map<String, Object> secondRequest = new HashMap<>();
            secondRequest.put(
                    "entries",
                    List.of(Map.of("fieldName", "제조국", "fieldValue", "대한민국", "sortOrder", 1)));

            // when
            givenAdmin()
                    .body(secondRequest)
                    .when()
                    .put(BASE_PATH + "/" + savedProductGroupId + "/notice")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // then - 2차 수정 후 항목 수 검증 (1개로 변경되어야 함)
            ProductNoticeJpaEntity noticeAfterSecond =
                    productNoticeJpaRepository
                            .findByProductGroupId(savedProductGroupId)
                            .orElseThrow(() -> new AssertionError("2차 수정 후 고시정보가 존재하지 않습니다."));
            List<ProductNoticeEntryJpaEntity> entriesAfterSecond =
                    productNoticeEntryJpaRepository.findByProductNoticeId(
                            noticeAfterSecond.getId());
            assertThat(entriesAfterSecond).hasSize(1);
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
                .post(BASE_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath()
                .getLong("data");
    }
}

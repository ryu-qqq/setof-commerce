package com.ryuqq.setof.application.productgroup.dto.composite;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.refundpolicy.dto.response.NonReturnableConditionResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupDetailCompositeQueryResult 단위 테스트")
class ProductGroupDetailCompositeQueryResultTest {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("record 생성 - 필드 매핑 검증")
    class RecordConstructionTest {

        @Test
        @DisplayName("모든 필드를 포함하여 올바르게 생성된다")
        void constructor_AllFields_MappedCorrectly() {
            // given
            ShippingPolicyResult shippingPolicy = shippingPolicyResult();
            RefundPolicyResult refundPolicy = refundPolicyResult();

            // when
            ProductGroupDetailCompositeQueryResult result =
                    new ProductGroupDetailCompositeQueryResult(
                            1L,
                            10L,
                            "테스트셀러",
                            20L,
                            "테스트브랜드",
                            "https://example.com/brand-icon.png",
                            30L,
                            "의류",
                            "1,2,30",
                            "테스트 상품 그룹",
                            "COMBINATION",
                            "ACTIVE",
                            FIXED_NOW,
                            FIXED_NOW,
                            shippingPolicy,
                            refundPolicy,
                            1L,
                            "<p>상세설명</p>",
                            "https://cdn.example.com/desc/1",
                            2L,
                            FIXED_NOW,
                            FIXED_NOW);

            // then
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.sellerId()).isEqualTo(10L);
            assertThat(result.sellerName()).isEqualTo("테스트셀러");
            assertThat(result.brandId()).isEqualTo(20L);
            assertThat(result.brandName()).isEqualTo("테스트브랜드");
            assertThat(result.categoryId()).isEqualTo(30L);
            assertThat(result.categoryName()).isEqualTo("의류");
            assertThat(result.categoryPath()).isEqualTo("1,2,30");
            assertThat(result.productGroupName()).isEqualTo("테스트 상품 그룹");
            assertThat(result.optionType()).isEqualTo("COMBINATION");
            assertThat(result.status()).isEqualTo("ACTIVE");
            assertThat(result.createdAt()).isEqualTo(FIXED_NOW);
            assertThat(result.updatedAt()).isEqualTo(FIXED_NOW);
            assertThat(result.shippingPolicy()).isEqualTo(shippingPolicy);
            assertThat(result.refundPolicy()).isEqualTo(refundPolicy);
        }

        @Test
        @DisplayName("shippingPolicy와 refundPolicy가 null이어도 생성된다")
        void constructor_NullPolicies_CreatedSuccessfully() {
            // given / when
            ProductGroupDetailCompositeQueryResult result =
                    new ProductGroupDetailCompositeQueryResult(
                            1L,
                            10L,
                            "테스트셀러",
                            20L,
                            "테스트브랜드",
                            "https://example.com/brand-icon.png",
                            30L,
                            "의류",
                            "1,2,30",
                            "테스트 상품 그룹",
                            "SINGLE",
                            "INACTIVE",
                            FIXED_NOW,
                            FIXED_NOW,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);

            // then
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.shippingPolicy()).isNull();
            assertThat(result.refundPolicy()).isNull();
        }
    }

    // ===== Helper =====

    private ShippingPolicyResult shippingPolicyResult() {
        return new ShippingPolicyResult(
                100L, "기본 배송 정책", true, true, "FIXED", "고정 배송비", 3000L, 50000L, FIXED_NOW);
    }

    private RefundPolicyResult refundPolicyResult() {
        return new RefundPolicyResult(
                200L,
                "기본 환불 정책",
                true,
                true,
                7,
                7,
                List.of(new NonReturnableConditionResult("USED", "사용한 상품")),
                FIXED_NOW);
    }
}

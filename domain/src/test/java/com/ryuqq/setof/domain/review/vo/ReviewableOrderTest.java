package com.ryuqq.setof.domain.review.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.review.ReviewFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewableOrder Value Object 테스트")
class ReviewableOrderTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("기본 ReviewableOrder를 생성한다")
        void createDefaultReviewableOrder() {
            // when
            ReviewableOrder order = ReviewFixtures.defaultReviewableOrder();

            // then
            assertThat(order).isNotNull();
            assertThat(order.legacyOrderId()).isEqualTo(700L);
            assertThat(order.orderId()).isEqualTo("order-uuid-0001");
            assertThat(order.orderStatus()).isEqualTo("DELIVERED");
            assertThat(order.productQuantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("옵션 목록이 있는 ReviewableOrder를 생성한다")
        void createReviewableOrderWithOptions() {
            // given
            List<ReviewableOrder.ReviewableOrderOption> options =
                    List.of(
                            ReviewableOrder.ReviewableOrderOption.of(100L, 200L, "사이즈", "M"),
                            ReviewableOrder.ReviewableOrderOption.of(101L, 201L, "색상", "블랙"));

            // when
            ReviewableOrder order = ReviewFixtures.reviewableOrderWithOptions(options);

            // then
            assertThat(order.options()).hasSize(2);
            assertThat(order.options().get(0).optionName()).isEqualTo("사이즈");
            assertThat(order.options().get(1).optionName()).isEqualTo("색상");
        }
    }

    @Nested
    @DisplayName("내부 스냅샷 레코드 테스트")
    class SnapshotTest {

        @Test
        @DisplayName("SellerSnapshot을 올바르게 생성한다")
        void createSellerSnapshot() {
            // when
            ReviewableOrder.SellerSnapshot snapshot =
                    new ReviewableOrder.SellerSnapshot(1L, "테스트 셀러");

            // then
            assertThat(snapshot.sellerId()).isEqualTo(1L);
            assertThat(snapshot.name()).isEqualTo("테스트 셀러");
        }

        @Test
        @DisplayName("ProductSnapshot을 올바르게 생성한다")
        void createProductSnapshot() {
            // when
            ReviewableOrder.ProductSnapshot snapshot =
                    new ReviewableOrder.ProductSnapshot(
                            10L, 500L, "테스트 상품", "https://example.com/image.jpg");

            // then
            assertThat(snapshot.productId()).isEqualTo(10L);
            assertThat(snapshot.productGroupId()).isEqualTo(500L);
            assertThat(snapshot.productGroupName()).isEqualTo("테스트 상품");
            assertThat(snapshot.productGroupMainImageUrl())
                    .isEqualTo("https://example.com/image.jpg");
        }

        @Test
        @DisplayName("BrandSnapshot을 올바르게 생성한다")
        void createBrandSnapshot() {
            // when
            ReviewableOrder.BrandSnapshot snapshot =
                    new ReviewableOrder.BrandSnapshot(20L, "테스트 브랜드");

            // then
            assertThat(snapshot.brandId()).isEqualTo(20L);
            assertThat(snapshot.name()).isEqualTo("테스트 브랜드");
        }

        @Test
        @DisplayName("ReviewableOrderOption을 of()로 생성한다")
        void createReviewableOrderOptionWithOf() {
            // when
            ReviewableOrder.ReviewableOrderOption option =
                    ReviewableOrder.ReviewableOrderOption.of(100L, 200L, "사이즈", "M");

            // then
            assertThat(option.optionGroupId()).isEqualTo(100L);
            assertThat(option.optionDetailId()).isEqualTo(200L);
            assertThat(option.optionName()).isEqualTo("사이즈");
            assertThat(option.optionValue()).isEqualTo("M");
        }
    }

    @Nested
    @DisplayName("가격 정보 테스트")
    class PriceTest {

        @Test
        @DisplayName("정가, 판매가, 주문금액을 올바르게 반환한다")
        void returnsPriceValues() {
            // when
            ReviewableOrder order = ReviewFixtures.defaultReviewableOrder();

            // then
            assertThat(order.regularPrice()).isEqualTo(30000L);
            assertThat(order.currentPrice()).isEqualTo(25000L);
            assertThat(order.orderAmount()).isEqualTo(25000L);
        }
    }
}

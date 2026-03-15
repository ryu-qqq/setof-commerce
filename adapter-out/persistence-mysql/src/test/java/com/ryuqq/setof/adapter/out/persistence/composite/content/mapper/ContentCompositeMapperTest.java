package com.ryuqq.setof.adapter.out.persistence.composite.content.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.AutoProductThumbnailDto;
import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.FixedProductThumbnailDto;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ContentCompositeMapperTest - 콘텐츠 Composite Mapper 단위 테스트.
 *
 * <p>FixedProductThumbnailDto, AutoProductThumbnailDto → ProductThumbnailSnapshot 변환 검증.
 *
 * <p>할인 계산 로직 (calcDirectDiscountRate, calcDirectDiscountPrice, calcDiscountRate) 검증.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentCompositeMapper 단위 테스트")
class ContentCompositeMapperTest {

    private ContentCompositeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ContentCompositeMapper();
    }

    // ========================================================================
    // 1. toSnapshot(FixedProductThumbnailDto) 테스트
    // ========================================================================

    @Nested
    @DisplayName("toSnapshot(FixedProductThumbnailDto) 메서드 테스트")
    class ToSnapshotFromFixedTest {

        @Test
        @DisplayName("기본 FixedDto를 올바르게 변환합니다")
        void toSnapshot_WithFixedDto_MapsFieldsCorrectly() {
            // given
            Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
            FixedProductThumbnailDto dto =
                    new FixedProductThumbnailDto(
                            10L,
                            null,
                            100L,
                            "전시명",
                            "https://display.img/a.jpg",
                            1L,
                            "상품명",
                            5L,
                            "브랜드A",
                            10000,
                            8000,
                            0,
                            createdAt,
                            "https://thumb.img/a.jpg");

            // when
            ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

            // then
            assertThat(snapshot.productGroupId()).isEqualTo(100L);
            assertThat(snapshot.sellerId()).isEqualTo(1L);
            assertThat(snapshot.productGroupName()).isEqualTo("상품명");
            assertThat(snapshot.brandId()).isEqualTo(5L);
            assertThat(snapshot.brandName()).isEqualTo("브랜드A");
            assertThat(snapshot.productImageUrl()).isEqualTo("https://thumb.img/a.jpg");
            assertThat(snapshot.displayName()).isEqualTo("전시명");
            assertThat(snapshot.displayImageUrl()).isEqualTo("https://display.img/a.jpg");
            assertThat(snapshot.regularPrice()).isEqualTo(10000);
            assertThat(snapshot.currentPrice()).isEqualTo(8000);
            assertThat(snapshot.salePrice()).isEqualTo(0);
            assertThat(snapshot.createdAt()).isEqualTo(createdAt);
            assertThat(snapshot.displayed()).isTrue();
            assertThat(snapshot.soldOut()).isFalse();
        }

        @Nested
        @DisplayName("직접 할인율(calcDirectDiscountRate) 계산 테스트")
        class CalcDirectDiscountRateTest {

            @Test
            @DisplayName("salePrice가 currentPrice보다 낮으면 직접 할인율을 계산합니다")
            void calcDirectDiscountRate_WhenSalePriceLowerThanCurrent_ReturnsRate() {
                // given - currentPrice=10000, salePrice=8000 → 할인율 = (10000-8000)*100/10000 = 20
                FixedProductThumbnailDto dto = fixedDto(10000, 10000, 8000);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountRate()).isEqualTo(20);
            }

            @Test
            @DisplayName("salePrice가 0이면 직접 할인율은 0입니다")
            void calcDirectDiscountRate_WhenSalePriceZero_ReturnsZero() {
                // given
                FixedProductThumbnailDto dto = fixedDto(10000, 10000, 0);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountRate()).isEqualTo(0);
            }

            @Test
            @DisplayName("salePrice가 currentPrice 이상이면 직접 할인율은 0입니다")
            void calcDirectDiscountRate_WhenSalePriceGteCurrentPrice_ReturnsZero() {
                // given
                FixedProductThumbnailDto dto = fixedDto(10000, 10000, 10000);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountRate()).isEqualTo(0);
            }

            @Test
            @DisplayName("currentPrice가 0이면 직접 할인율은 0입니다")
            void calcDirectDiscountRate_WhenCurrentPriceZero_ReturnsZero() {
                // given
                FixedProductThumbnailDto dto = fixedDto(10000, 0, 0);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountRate()).isEqualTo(0);
            }
        }

        @Nested
        @DisplayName("직접 할인금액(calcDirectDiscountPrice) 계산 테스트")
        class CalcDirectDiscountPriceTest {

            @Test
            @DisplayName("salePrice가 currentPrice보다 낮으면 직접 할인금액을 계산합니다")
            void calcDirectDiscountPrice_WhenSalePriceLowerThanCurrent_ReturnsPrice() {
                // given - currentPrice=10000, salePrice=8000 → 할인금액 = 10000-8000 = 2000
                FixedProductThumbnailDto dto = fixedDto(10000, 10000, 8000);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountPrice()).isEqualTo(2000);
            }

            @Test
            @DisplayName("salePrice가 0이면 직접 할인금액은 0입니다")
            void calcDirectDiscountPrice_WhenSalePriceZero_ReturnsZero() {
                // given
                FixedProductThumbnailDto dto = fixedDto(10000, 10000, 0);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountPrice()).isEqualTo(0);
            }

            @Test
            @DisplayName("salePrice가 currentPrice와 동일하면 직접 할인금액은 0입니다")
            void calcDirectDiscountPrice_WhenSalePriceEqualCurrentPrice_ReturnsZero() {
                // given
                FixedProductThumbnailDto dto = fixedDto(10000, 10000, 10000);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountPrice()).isEqualTo(0);
            }
        }

        @Nested
        @DisplayName("총 할인율(calcDiscountRate) 계산 테스트")
        class CalcDiscountRateForFixedTest {

            @Test
            @DisplayName("salePrice가 유효하면 effectivePrice=salePrice로 총 할인율을 계산합니다")
            void calcDiscountRate_WithEffectiveSalePrice_UsessSalePrice() {
                // given
                // regularPrice=10000, currentPrice=9000, salePrice=7000
                // effectivePrice = salePrice(7000) < currentPrice(9000) → 7000
                // discountRate = (10000-7000)*100/10000 = 30
                FixedProductThumbnailDto dto = fixedDto(10000, 9000, 7000);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.discountRate()).isEqualTo(30);
            }

            @Test
            @DisplayName("salePrice가 없으면 effectivePrice=currentPrice로 총 할인율을 계산합니다")
            void calcDiscountRate_WithNoSalePrice_UsesCurrentPrice() {
                // given
                // regularPrice=10000, currentPrice=8000, salePrice=0
                // effectivePrice = 8000 (salePrice=0, not valid)
                // discountRate = (10000-8000)*100/10000 = 20
                FixedProductThumbnailDto dto = fixedDto(10000, 8000, 0);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.discountRate()).isEqualTo(20);
            }

            @Test
            @DisplayName("regularPrice가 0이면 총 할인율은 0입니다")
            void calcDiscountRate_WhenRegularPriceZero_ReturnsZero() {
                // given
                FixedProductThumbnailDto dto = fixedDto(0, 8000, 0);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.discountRate()).isEqualTo(0);
            }

            @Test
            @DisplayName("effectivePrice가 regularPrice 이상이면 총 할인율은 0입니다")
            void calcDiscountRate_WhenEffectivePriceGteRegularPrice_ReturnsZero() {
                // given
                FixedProductThumbnailDto dto = fixedDto(8000, 10000, 0);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.discountRate()).isEqualTo(0);
            }
        }
    }

    // ========================================================================
    // 2. toSnapshot(AutoProductThumbnailDto) 테스트
    // ========================================================================

    @Nested
    @DisplayName("toSnapshot(AutoProductThumbnailDto) 메서드 테스트")
    class ToSnapshotFromAutoTest {

        @Test
        @DisplayName("기본 AutoDto를 올바르게 변환합니다")
        void toSnapshot_WithAutoDto_MapsFieldsCorrectly() {
            // given
            Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
            AutoProductThumbnailDto dto =
                    new AutoProductThumbnailDto(
                            100L,
                            1L,
                            "상품명",
                            5L,
                            "브랜드A",
                            10000,
                            8000,
                            0,
                            createdAt,
                            "https://thumb.img/a.jpg");

            // when
            ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

            // then
            assertThat(snapshot.productGroupId()).isEqualTo(100L);
            assertThat(snapshot.sellerId()).isEqualTo(1L);
            assertThat(snapshot.productGroupName()).isEqualTo("상품명");
            assertThat(snapshot.brandId()).isEqualTo(5L);
            assertThat(snapshot.brandName()).isEqualTo("브랜드A");
            assertThat(snapshot.productImageUrl()).isEqualTo("https://thumb.img/a.jpg");
            assertThat(snapshot.displayName()).isNull();
            assertThat(snapshot.displayImageUrl()).isNull();
            assertThat(snapshot.regularPrice()).isEqualTo(10000);
            assertThat(snapshot.currentPrice()).isEqualTo(8000);
            assertThat(snapshot.createdAt()).isEqualTo(createdAt);
            assertThat(snapshot.displayed()).isTrue();
            assertThat(snapshot.soldOut()).isFalse();
        }

        @Test
        @DisplayName("AutoDto에서 직접 할인율을 올바르게 계산합니다")
        void toSnapshot_WithAutoDto_CalculatesDirectDiscountRateCorrectly() {
            // given - currentPrice=10000, salePrice=7000 → 직접할인율 = 30
            AutoProductThumbnailDto dto = autoDto(10000, 10000, 7000);

            // when
            ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

            // then
            assertThat(snapshot.directDiscountRate()).isEqualTo(30);
            assertThat(snapshot.directDiscountPrice()).isEqualTo(3000);
        }

        @Test
        @DisplayName("AutoDto에서 총 할인율을 올바르게 계산합니다")
        void toSnapshot_WithAutoDto_CalculatesDiscountRateCorrectly() {
            // given - regularPrice=10000, currentPrice=8000, salePrice=7000
            // effectivePrice=7000, discountRate=(10000-7000)*100/10000=30
            AutoProductThumbnailDto dto = autoDto(10000, 8000, 7000);

            // when
            ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

            // then
            assertThat(snapshot.discountRate()).isEqualTo(30);
        }

        @Nested
        @DisplayName("AutoDto 할인 계산 경계값 테스트")
        class AutoDiscountBoundaryTest {

            @Test
            @DisplayName("salePrice가 없을 때 직접 할인율은 0입니다")
            void toSnapshot_WithNoSalePrice_DirectDiscountRateIsZero() {
                // given
                AutoProductThumbnailDto dto = autoDto(10000, 8000, 0);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountRate()).isEqualTo(0);
                assertThat(snapshot.directDiscountPrice()).isEqualTo(0);
            }

            @Test
            @DisplayName("salePrice가 currentPrice보다 크면 직접 할인율은 0입니다")
            void toSnapshot_WhenSalePriceGtCurrentPrice_DirectDiscountRateIsZero() {
                // given
                AutoProductThumbnailDto dto = autoDto(10000, 8000, 9000);

                // when
                ProductThumbnailSnapshot snapshot = mapper.toSnapshot(dto);

                // then
                assertThat(snapshot.directDiscountRate()).isEqualTo(0);
            }
        }
    }

    // ========================================================================
    // 헬퍼 메서드
    // ========================================================================

    private FixedProductThumbnailDto fixedDto(int regularPrice, int currentPrice, int salePrice) {
        return new FixedProductThumbnailDto(
                10L,
                null,
                100L,
                "전시명",
                "https://display.img/a.jpg",
                1L,
                "상품명",
                5L,
                "브랜드A",
                regularPrice,
                currentPrice,
                salePrice,
                Instant.parse("2025-01-01T00:00:00Z"),
                "https://thumb.img/a.jpg");
    }

    private AutoProductThumbnailDto autoDto(int regularPrice, int currentPrice, int salePrice) {
        return new AutoProductThumbnailDto(
                100L,
                1L,
                "상품명",
                5L,
                "브랜드A",
                regularPrice,
                currentPrice,
                salePrice,
                Instant.parse("2025-01-01T00:00:00Z"),
                "https://thumb.img/a.jpg");
    }
}

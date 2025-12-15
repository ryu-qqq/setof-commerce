package com.ryuqq.setof.domain.product.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.product.ProductFixture;
import com.ryuqq.setof.domain.product.vo.Money;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Product Entity 테스트
 *
 * <p>개별 SKU 정보 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("Product Entity")
class ProductTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("createSingle() - 단품 SKU 생성")
    class CreateSingle {

        @Test
        @DisplayName("단품 SKU를 생성할 수 있다")
        void shouldCreateSingleProduct() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            Money additionalPrice = Money.of(BigDecimal.valueOf(1000));

            // when
            Product product = Product.createSingle(productGroupId, additionalPrice, FIXED_TIME);

            // then
            assertNotNull(product);
            assertNotNull(product.getId());
            assertTrue(product.getId().isNew());
            assertEquals(1L, product.getProductGroupIdValue());
            assertEquals(OptionType.SINGLE, product.getOptionType());
            assertNull(product.getOption1Name());
            assertNull(product.getOption1Value());
            assertNull(product.getOption2Name());
            assertNull(product.getOption2Value());
            assertFalse(product.hasOption());
            assertFalse(product.isSoldOut());
            assertTrue(product.isDisplayed());
            assertTrue(product.isSellable());
        }

        @Test
        @DisplayName("추가금액 없이 단품 SKU를 생성할 수 있다")
        void shouldCreateSingleProductWithoutAdditionalPrice() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);

            // when
            Product product = Product.createSingle(productGroupId, null, FIXED_TIME);

            // then
            assertNotNull(product);
            assertEquals(BigDecimal.ZERO, product.getAdditionalPriceValue());
            assertFalse(product.hasAdditionalPrice());
        }

        @Test
        @DisplayName("ProductGroupId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenProductGroupIdIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Product.createSingle(null, Money.zero(), FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("createOneLevel() - 1단 옵션 SKU 생성")
    class CreateOneLevel {

        @Test
        @DisplayName("1단 옵션 SKU를 생성할 수 있다")
        void shouldCreateOneLevelProduct() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            String option1Name = "색상";
            String option1Value = "블랙";
            Money additionalPrice = Money.of(BigDecimal.valueOf(500));

            // when
            Product product =
                    Product.createOneLevel(
                            productGroupId, option1Name, option1Value, additionalPrice, FIXED_TIME);

            // then
            assertNotNull(product);
            assertEquals(OptionType.ONE_LEVEL, product.getOptionType());
            assertEquals("색상", product.getOption1Name());
            assertEquals("블랙", product.getOption1Value());
            assertNull(product.getOption2Name());
            assertNull(product.getOption2Value());
            assertTrue(product.hasOption());
            assertFalse(product.isTwoLevelOption());
        }

        @Test
        @DisplayName("옵션1 이름이 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenOption1NameIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Product.createOneLevel(
                                    ProductGroupId.of(1L), null, "블랙", Money.zero(), FIXED_TIME));
        }

        @Test
        @DisplayName("옵션1 값이 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenOption1ValueIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Product.createOneLevel(
                                    ProductGroupId.of(1L), "색상", null, Money.zero(), FIXED_TIME));
        }

        @Test
        @DisplayName("옵션1 이름이 빈 문자열이면 예외가 발생한다")
        void shouldThrowExceptionWhenOption1NameIsBlank() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Product.createOneLevel(
                                    ProductGroupId.of(1L), "  ", "블랙", Money.zero(), FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("createTwoLevel() - 2단 옵션 SKU 생성")
    class CreateTwoLevel {

        @Test
        @DisplayName("2단 옵션 SKU를 생성할 수 있다")
        void shouldCreateTwoLevelProduct() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            String option1Name = "색상";
            String option1Value = "블랙";
            String option2Name = "사이즈";
            String option2Value = "M";
            Money additionalPrice = Money.of(BigDecimal.valueOf(1000));

            // when
            Product product =
                    Product.createTwoLevel(
                            productGroupId,
                            option1Name,
                            option1Value,
                            option2Name,
                            option2Value,
                            additionalPrice,
                            FIXED_TIME);

            // then
            assertNotNull(product);
            assertEquals(OptionType.TWO_LEVEL, product.getOptionType());
            assertEquals("색상", product.getOption1Name());
            assertEquals("블랙", product.getOption1Value());
            assertEquals("사이즈", product.getOption2Name());
            assertEquals("M", product.getOption2Value());
            assertTrue(product.hasOption());
            assertTrue(product.isTwoLevelOption());
        }

        @Test
        @DisplayName("옵션2 이름이 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenOption2NameIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Product.createTwoLevel(
                                    ProductGroupId.of(1L),
                                    "색상",
                                    "블랙",
                                    null,
                                    "M",
                                    Money.zero(),
                                    FIXED_TIME));
        }

        @Test
        @DisplayName("옵션2 값이 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenOption2ValueIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Product.createTwoLevel(
                                    ProductGroupId.of(1L),
                                    "색상",
                                    "블랙",
                                    "사이즈",
                                    null,
                                    Money.zero(),
                                    FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteProductFromPersistence() {
            // given
            ProductId id = ProductId.of(1L);
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            Product product =
                    Product.reconstitute(
                            id,
                            productGroupId,
                            OptionType.TWO_LEVEL,
                            "색상",
                            "블랙",
                            "사이즈",
                            "M",
                            Money.of(BigDecimal.valueOf(1000)),
                            ProductStatus.defaultStatus(),
                            createdAt,
                            updatedAt,
                            null);

            // then
            assertEquals(1L, product.getIdValue());
            assertEquals(1L, product.getProductGroupIdValue());
            assertEquals("TWO_LEVEL", product.getOptionTypeValue());
            assertFalse(product.isSoldOut());
            assertTrue(product.isDisplayed());
            assertFalse(product.isDeleted());
            assertEquals(createdAt, product.getCreatedAt());
            assertEquals(updatedAt, product.getUpdatedAt());
        }

        @Test
        @DisplayName("삭제된 상품을 복원할 수 있다")
        void shouldReconstituteDeletedProduct() {
            // given
            Instant deletedAt = Instant.parse("2024-12-01T00:00:00Z");

            // when
            Product product =
                    Product.reconstitute(
                            ProductId.of(1L),
                            ProductGroupId.of(1L),
                            OptionType.TWO_LEVEL,
                            "색상",
                            "블랙",
                            "사이즈",
                            "M",
                            Money.of(BigDecimal.valueOf(1000)),
                            ProductStatus.of(false, false),
                            FIXED_TIME,
                            deletedAt,
                            deletedAt);

            // then
            assertTrue(product.isDeleted());
            assertEquals(deletedAt, product.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("updateOption() - 옵션 정보 업데이트")
    class UpdateOption {

        @Test
        @DisplayName("옵션 정보를 업데이트할 수 있다")
        void shouldUpdateOption() {
            // given
            Product product = ProductFixture.create();
            String newOption1Name = "컬러";
            String newOption1Value = "화이트";
            String newOption2Name = "크기";
            String newOption2Value = "L";
            Money newAdditionalPrice = Money.of(BigDecimal.valueOf(2000));

            // when
            Product updatedProduct =
                    product.updateOption(
                            newOption1Name, newOption1Value,
                            newOption2Name, newOption2Value,
                            newAdditionalPrice, FIXED_TIME);

            // then
            assertEquals("컬러", updatedProduct.getOption1Name());
            assertEquals("화이트", updatedProduct.getOption1Value());
            assertEquals("크기", updatedProduct.getOption2Name());
            assertEquals("L", updatedProduct.getOption2Value());
            assertEquals(BigDecimal.valueOf(2000), updatedProduct.getAdditionalPriceValue());
            // 옵션 타입과 상태는 변경되지 않음
            assertEquals(product.getOptionType(), updatedProduct.getOptionType());
            assertEquals(product.getStatus(), updatedProduct.getStatus());
        }
    }

    @Nested
    @DisplayName("markSoldOut() / markInStock() - 품절/재고 상태 변경")
    class StockStatus {

        @Test
        @DisplayName("상품을 품절 처리할 수 있다")
        void shouldMarkProductAsSoldOut() {
            // given
            Product product = ProductFixture.create();
            assertFalse(product.isSoldOut());

            // when
            Product soldOutProduct = product.markSoldOut(FIXED_TIME);

            // then
            assertTrue(soldOutProduct.isSoldOut());
            assertFalse(soldOutProduct.isSellable());
        }

        @Test
        @DisplayName("품절 상품을 재고 있음으로 변경할 수 있다")
        void shouldMarkProductAsInStock() {
            // given
            Product soldOutProduct = ProductFixture.createSoldOut();
            assertTrue(soldOutProduct.isSoldOut());

            // when
            Product inStockProduct = soldOutProduct.markInStock(FIXED_TIME);

            // then
            assertFalse(inStockProduct.isSoldOut());
            assertTrue(inStockProduct.isSellable());
        }
    }

    @Nested
    @DisplayName("show() / hide() - 노출 상태 변경")
    class DisplayStatus {

        @Test
        @DisplayName("상품을 숨김 처리할 수 있다")
        void shouldHideProduct() {
            // given
            Product product = ProductFixture.create();
            assertTrue(product.isDisplayed());

            // when
            Product hiddenProduct = product.hide(FIXED_TIME);

            // then
            assertFalse(hiddenProduct.isDisplayed());
            assertFalse(hiddenProduct.isSellable());
        }

        @Test
        @DisplayName("숨김 상품을 노출 처리할 수 있다")
        void shouldShowProduct() {
            // given
            Product hiddenProduct = ProductFixture.createHidden();
            assertFalse(hiddenProduct.isDisplayed());

            // when
            Product shownProduct = hiddenProduct.show(FIXED_TIME);

            // then
            assertTrue(shownProduct.isDisplayed());
        }
    }

    @Nested
    @DisplayName("delete() - 상품 삭제")
    class Delete {

        @Test
        @DisplayName("상품을 소프트 삭제할 수 있다")
        void shouldSoftDeleteProduct() {
            // given
            Product product = ProductFixture.create();
            assertFalse(product.isDeleted());

            // when
            Product deletedProduct = product.delete(FIXED_TIME);

            // then
            assertTrue(deletedProduct.isDeleted());
            assertNotNull(deletedProduct.getDeletedAt());
            assertFalse(deletedProduct.isDisplayed());
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusCheckMethods {

        @Test
        @DisplayName("isSellable()은 품절이 아니고 노출 중일 때 true를 반환한다")
        void shouldReturnTrueWhenSellable() {
            // given
            Product product = ProductFixture.create();

            // then
            assertTrue(product.isSellable());
        }

        @Test
        @DisplayName("isSellable()은 품절이면 false를 반환한다")
        void shouldReturnFalseWhenSoldOut() {
            // given
            Product soldOutProduct = ProductFixture.createSoldOut();

            // then
            assertFalse(soldOutProduct.isSellable());
        }

        @Test
        @DisplayName("isSellable()은 숨김이면 false를 반환한다")
        void shouldReturnFalseWhenHidden() {
            // given
            Product hiddenProduct = ProductFixture.createHidden();

            // then
            assertFalse(hiddenProduct.isSellable());
        }

        @Test
        @DisplayName("hasOption()은 옵션이 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasOption() {
            // given
            Product oneLevelProduct = ProductFixture.createOneLevel();
            Product twoLevelProduct = ProductFixture.createTwoLevel();

            // then
            assertTrue(oneLevelProduct.hasOption());
            assertTrue(twoLevelProduct.hasOption());
        }

        @Test
        @DisplayName("hasOption()은 단품이면 false를 반환한다")
        void shouldReturnFalseWhenSingleProduct() {
            // given
            Product singleProduct = ProductFixture.createSingle();

            // then
            assertFalse(singleProduct.hasOption());
        }

        @Test
        @DisplayName("hasAdditionalPrice()는 추가금액이 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasAdditionalPrice() {
            // given
            Product product = ProductFixture.create();

            // then
            assertTrue(product.hasAdditionalPrice());
        }

        @Test
        @DisplayName("hasAdditionalPrice()는 추가금액이 0이면 false를 반환한다")
        void shouldReturnFalseWhenNoAdditionalPrice() {
            // given
            Product product = ProductFixture.createWithoutAdditionalPrice();

            // then
            assertFalse(product.hasAdditionalPrice());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID 값을 반환한다")
        void shouldReturnIdValue() {
            // given
            Product product = ProductFixture.createWithId(100L);

            // then
            assertEquals(100L, product.getIdValue());
        }

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            Product product = ProductFixture.createNewSingle(ProductGroupId.of(1L));

            // then
            assertNull(product.getIdValue());
            assertFalse(product.hasId());
        }

        @Test
        @DisplayName("getProductGroupIdValue()는 상품그룹 ID 값을 반환한다")
        void shouldReturnProductGroupIdValue() {
            // given
            Product product = ProductFixture.create();

            // then
            assertEquals(1L, product.getProductGroupIdValue());
        }

        @Test
        @DisplayName("getOptionTypeValue()는 옵션 타입 문자열을 반환한다")
        void shouldReturnOptionTypeValue() {
            // given
            Product singleProduct = ProductFixture.createSingle();
            Product oneLevelProduct = ProductFixture.createOneLevel();
            Product twoLevelProduct = ProductFixture.createTwoLevel();

            // then
            assertEquals("SINGLE", singleProduct.getOptionTypeValue());
            assertEquals("ONE_LEVEL", oneLevelProduct.getOptionTypeValue());
            assertEquals("TWO_LEVEL", twoLevelProduct.getOptionTypeValue());
        }

        @Test
        @DisplayName("getAdditionalPriceValue()는 추가금액을 반환한다")
        void shouldReturnAdditionalPriceValue() {
            // given
            Product product = ProductFixture.create();

            // then
            assertEquals(BigDecimal.valueOf(1000), product.getAdditionalPriceValue());
        }

        @Test
        @DisplayName("getSoldOutValue()는 품절 여부를 반환한다")
        void shouldReturnSoldOutValue() {
            // given
            Product product = ProductFixture.create();
            Product soldOutProduct = ProductFixture.createSoldOut();

            // then
            assertFalse(product.getSoldOutValue());
            assertTrue(soldOutProduct.getSoldOutValue());
        }

        @Test
        @DisplayName("getDisplayYnValue()는 노출 여부를 반환한다")
        void shouldReturnDisplayYnValue() {
            // given
            Product product = ProductFixture.create();
            Product hiddenProduct = ProductFixture.createHidden();

            // then
            assertTrue(product.getDisplayYnValue());
            assertFalse(hiddenProduct.getDisplayYnValue());
        }
    }
}

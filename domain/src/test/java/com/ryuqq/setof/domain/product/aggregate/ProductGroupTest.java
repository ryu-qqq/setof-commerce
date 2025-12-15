package com.ryuqq.setof.domain.product.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.product.ProductGroupFixture;
import com.ryuqq.setof.domain.product.exception.ProductGroupNotEditableException;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.product.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ProductGroup Aggregate Root 테스트
 *
 * <p>상품그룹 정보 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("ProductGroup Aggregate Root")
class ProductGroupTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("create() - 신규 상품그룹 생성")
    class Create {

        @Test
        @DisplayName("신규 상품그룹을 생성할 수 있다")
        void shouldCreateNewProductGroup() {
            // given
            SellerId sellerId = SellerId.of(1L);
            CategoryId categoryId = CategoryId.of(100L);
            BrandId brandId = BrandId.of(1L);
            ProductGroupName name = ProductGroupName.of("테스트 상품그룹");
            OptionType optionType = OptionType.TWO_LEVEL;
            Price price = Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000));
            ShippingPolicyId shippingPolicyId = ShippingPolicyId.of(1L);
            RefundPolicyId refundPolicyId = RefundPolicyId.of(1L);

            // when
            ProductGroup productGroup =
                    ProductGroup.create(
                            sellerId,
                            categoryId,
                            brandId,
                            name,
                            optionType,
                            price,
                            shippingPolicyId,
                            refundPolicyId,
                            FIXED_TIME);

            // then
            assertNotNull(productGroup);
            assertNotNull(productGroup.getId());
            assertTrue(productGroup.getId().isNew());
            assertEquals(1L, productGroup.getSellerIdValue());
            assertEquals(100L, productGroup.getCategoryIdValue());
            assertEquals(1L, productGroup.getBrandIdValue());
            assertEquals("테스트 상품그룹", productGroup.getNameValue());
            assertEquals("TWO_LEVEL", productGroup.getOptionTypeValue());
            assertEquals(ProductGroupStatus.ACTIVE, productGroup.getStatus());
            assertTrue(productGroup.isActive());
            assertFalse(productGroup.isDeleted());
            assertNotNull(productGroup.getCreatedAt());
            assertNotNull(productGroup.getUpdatedAt());
        }

        @Test
        @DisplayName("정책 없이 상품그룹을 생성할 수 있다 (셀러 기본 정책 사용)")
        void shouldCreateProductGroupWithoutPolicies() {
            // given
            SellerId sellerId = SellerId.of(1L);
            CategoryId categoryId = CategoryId.of(100L);
            BrandId brandId = BrandId.of(1L);
            ProductGroupName name = ProductGroupName.of("정책없음 상품그룹");
            OptionType optionType = OptionType.SINGLE;
            Price price = Price.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(30000));

            // when
            ProductGroup productGroup =
                    ProductGroup.create(
                            sellerId,
                            categoryId,
                            brandId,
                            name,
                            optionType,
                            price,
                            null,
                            null,
                            FIXED_TIME);

            // then
            assertNotNull(productGroup);
            assertNull(productGroup.getShippingPolicyIdValue());
            assertNull(productGroup.getRefundPolicyIdValue());
            assertFalse(productGroup.hasCustomShippingPolicy());
            assertFalse(productGroup.hasCustomRefundPolicy());
        }

        @Test
        @DisplayName("필수 값이 없으면 예외가 발생한다 - SellerId null")
        void shouldThrowExceptionWhenSellerIdIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            ProductGroup.create(
                                    null,
                                    CategoryId.of(100L),
                                    BrandId.of(1L),
                                    ProductGroupName.of("테스트"),
                                    OptionType.SINGLE,
                                    Price.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(10000)),
                                    null,
                                    null,
                                    FIXED_TIME));
        }

        @Test
        @DisplayName("필수 값이 없으면 예외가 발생한다 - CategoryId null")
        void shouldThrowExceptionWhenCategoryIdIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            ProductGroup.create(
                                    SellerId.of(1L),
                                    null,
                                    BrandId.of(1L),
                                    ProductGroupName.of("테스트"),
                                    OptionType.SINGLE,
                                    Price.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(10000)),
                                    null,
                                    null,
                                    FIXED_TIME));
        }

        @Test
        @DisplayName("필수 값이 없으면 예외가 발생한다 - BrandId null")
        void shouldThrowExceptionWhenBrandIdIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            ProductGroup.create(
                                    SellerId.of(1L),
                                    CategoryId.of(100L),
                                    null,
                                    ProductGroupName.of("테스트"),
                                    OptionType.SINGLE,
                                    Price.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(10000)),
                                    null,
                                    null,
                                    FIXED_TIME));
        }

        @Test
        @DisplayName("필수 값이 없으면 예외가 발생한다 - ProductGroupName null")
        void shouldThrowExceptionWhenNameIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            ProductGroup.create(
                                    SellerId.of(1L),
                                    CategoryId.of(100L),
                                    BrandId.of(1L),
                                    null,
                                    OptionType.SINGLE,
                                    Price.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(10000)),
                                    null,
                                    null,
                                    FIXED_TIME));
        }

        @Test
        @DisplayName("필수 값이 없으면 예외가 발생한다 - OptionType null")
        void shouldThrowExceptionWhenOptionTypeIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            ProductGroup.create(
                                    SellerId.of(1L),
                                    CategoryId.of(100L),
                                    BrandId.of(1L),
                                    ProductGroupName.of("테스트"),
                                    null,
                                    Price.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(10000)),
                                    null,
                                    null,
                                    FIXED_TIME));
        }

        @Test
        @DisplayName("필수 값이 없으면 예외가 발생한다 - Price null")
        void shouldThrowExceptionWhenPriceIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            ProductGroup.create(
                                    SellerId.of(1L),
                                    CategoryId.of(100L),
                                    BrandId.of(1L),
                                    ProductGroupName.of("테스트"),
                                    OptionType.SINGLE,
                                    null,
                                    null,
                                    null,
                                    FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteProductGroupFromPersistence() {
            // given
            ProductGroupId id = ProductGroupId.of(1L);
            SellerId sellerId = SellerId.of(1L);
            CategoryId categoryId = CategoryId.of(100L);
            BrandId brandId = BrandId.of(1L);
            ProductGroupName name = ProductGroupName.of("복원된 상품그룹");
            OptionType optionType = OptionType.TWO_LEVEL;
            Price price = Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000));
            ShippingPolicyId shippingPolicyId = ShippingPolicyId.of(1L);
            RefundPolicyId refundPolicyId = RefundPolicyId.of(1L);
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            ProductGroup productGroup =
                    ProductGroup.reconstitute(
                            id,
                            sellerId,
                            categoryId,
                            brandId,
                            name,
                            optionType,
                            price,
                            ProductGroupStatus.ACTIVE,
                            shippingPolicyId,
                            refundPolicyId,
                            createdAt,
                            updatedAt,
                            null);

            // then
            assertEquals(1L, productGroup.getIdValue());
            assertEquals("복원된 상품그룹", productGroup.getNameValue());
            assertTrue(productGroup.isActive());
            assertFalse(productGroup.isDeleted());
            assertTrue(productGroup.hasCustomShippingPolicy());
            assertTrue(productGroup.hasCustomRefundPolicy());
            assertEquals(createdAt, productGroup.getCreatedAt());
            assertEquals(updatedAt, productGroup.getUpdatedAt());
        }

        @Test
        @DisplayName("삭제된 상품그룹을 복원할 수 있다")
        void shouldReconstituteDeletedProductGroup() {
            // given
            Instant deletedAt = Instant.parse("2024-12-01T00:00:00Z");

            // when
            ProductGroup productGroup =
                    ProductGroup.reconstitute(
                            ProductGroupId.of(1L),
                            SellerId.of(1L),
                            CategoryId.of(100L),
                            BrandId.of(1L),
                            ProductGroupName.of("삭제된 상품그룹"),
                            OptionType.TWO_LEVEL,
                            Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000)),
                            ProductGroupStatus.DELETED,
                            null,
                            null,
                            FIXED_TIME,
                            deletedAt,
                            deletedAt);

            // then
            assertTrue(productGroup.isDeleted());
            assertEquals(deletedAt, productGroup.getDeletedAt());
            assertFalse(productGroup.isEditable());
        }
    }

    @Nested
    @DisplayName("update() - 상품그룹 정보 수정")
    class Update {

        @Test
        @DisplayName("상품그룹 정보를 수정할 수 있다")
        void shouldUpdateProductGroup() {
            // given
            ProductGroup productGroup = ProductGroupFixture.create();
            CategoryId newCategoryId = CategoryId.of(200L);
            BrandId newBrandId = BrandId.of(2L);
            ProductGroupName newName = ProductGroupName.of("수정된 상품그룹");
            Price newPrice = Price.of(BigDecimal.valueOf(60000), BigDecimal.valueOf(55000));
            ShippingPolicyId newShippingPolicyId = ShippingPolicyId.of(2L);
            RefundPolicyId newRefundPolicyId = RefundPolicyId.of(2L);

            // when
            ProductGroup updatedProductGroup =
                    productGroup.update(
                            newCategoryId,
                            newBrandId,
                            newName,
                            newPrice,
                            newShippingPolicyId,
                            newRefundPolicyId,
                            FIXED_TIME);

            // then
            assertEquals(200L, updatedProductGroup.getCategoryIdValue());
            assertEquals(2L, updatedProductGroup.getBrandIdValue());
            assertEquals("수정된 상품그룹", updatedProductGroup.getNameValue());
            assertEquals(BigDecimal.valueOf(60000), updatedProductGroup.getPrice().regularPrice());
            assertEquals(BigDecimal.valueOf(55000), updatedProductGroup.getPrice().currentPrice());
            // 셀러 ID와 옵션 타입은 변경되지 않음
            assertEquals(productGroup.getSellerIdValue(), updatedProductGroup.getSellerIdValue());
            assertEquals(productGroup.getOptionType(), updatedProductGroup.getOptionType());
            // 상태도 유지됨
            assertEquals(productGroup.getStatus(), updatedProductGroup.getStatus());
        }

        @Test
        @DisplayName("삭제된 상품그룹은 수정할 수 없다")
        void shouldThrowExceptionWhenUpdatingDeletedProductGroup() {
            // given
            ProductGroup deletedProductGroup = ProductGroupFixture.createDeleted();

            // when & then
            assertThrows(
                    ProductGroupNotEditableException.class,
                    () ->
                            deletedProductGroup.update(
                                    CategoryId.of(200L),
                                    BrandId.of(2L),
                                    ProductGroupName.of("수정시도"),
                                    Price.of(BigDecimal.valueOf(60000), BigDecimal.valueOf(55000)),
                                    null,
                                    null,
                                    FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("changeStatus() / activate() / deactivate() - 상태 변경")
    class StatusChange {

        @Test
        @DisplayName("상태를 변경할 수 있다")
        void shouldChangeStatus() {
            // given
            ProductGroup productGroup = ProductGroupFixture.create();
            assertEquals(ProductGroupStatus.ACTIVE, productGroup.getStatus());

            // when
            ProductGroup inactiveProductGroup =
                    productGroup.changeStatus(ProductGroupStatus.INACTIVE, FIXED_TIME);

            // then
            assertEquals(ProductGroupStatus.INACTIVE, inactiveProductGroup.getStatus());
            assertFalse(inactiveProductGroup.isActive());
        }

        @Test
        @DisplayName("비활성 상품그룹을 활성화할 수 있다")
        void shouldActivateInactiveProductGroup() {
            // given
            ProductGroup inactiveProductGroup = ProductGroupFixture.createInactive();
            assertFalse(inactiveProductGroup.isActive());

            // when
            ProductGroup activatedProductGroup = inactiveProductGroup.activate(FIXED_TIME);

            // then
            assertTrue(activatedProductGroup.isActive());
            assertEquals(ProductGroupStatus.ACTIVE, activatedProductGroup.getStatus());
        }

        @Test
        @DisplayName("활성 상품그룹을 비활성화할 수 있다")
        void shouldDeactivateActiveProductGroup() {
            // given
            ProductGroup activeProductGroup = ProductGroupFixture.create();
            assertTrue(activeProductGroup.isActive());

            // when
            ProductGroup deactivatedProductGroup = activeProductGroup.deactivate(FIXED_TIME);

            // then
            assertFalse(deactivatedProductGroup.isActive());
            assertEquals(ProductGroupStatus.INACTIVE, deactivatedProductGroup.getStatus());
        }

        @Test
        @DisplayName("삭제된 상품그룹은 상태를 변경할 수 없다")
        void shouldThrowExceptionWhenChangingStatusOfDeletedProductGroup() {
            // given
            ProductGroup deletedProductGroup = ProductGroupFixture.createDeleted();

            // when & then
            assertThrows(
                    ProductGroupNotEditableException.class,
                    () -> deletedProductGroup.changeStatus(ProductGroupStatus.ACTIVE, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("delete() - 상품그룹 삭제")
    class Delete {

        @Test
        @DisplayName("상품그룹을 소프트 삭제할 수 있다")
        void shouldSoftDeleteProductGroup() {
            // given
            ProductGroup productGroup = ProductGroupFixture.create();
            assertFalse(productGroup.isDeleted());

            // when
            ProductGroup deletedProductGroup = productGroup.delete(FIXED_TIME);

            // then
            assertTrue(deletedProductGroup.isDeleted());
            assertEquals(ProductGroupStatus.DELETED, deletedProductGroup.getStatus());
            assertNotNull(deletedProductGroup.getDeletedAt());
            assertFalse(deletedProductGroup.isEditable());
            assertFalse(deletedProductGroup.isSellable());
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusCheckMethods {

        @Test
        @DisplayName("isActive()는 ACTIVE 상태일 때 true를 반환한다")
        void shouldReturnTrueWhenStatusIsActive() {
            // given
            ProductGroup productGroup = ProductGroupFixture.create();

            // then
            assertTrue(productGroup.isActive());
        }

        @Test
        @DisplayName("isActive()는 INACTIVE 상태일 때 false를 반환한다")
        void shouldReturnFalseWhenStatusIsInactive() {
            // given
            ProductGroup productGroup = ProductGroupFixture.createInactive();

            // then
            assertFalse(productGroup.isActive());
        }

        @Test
        @DisplayName("isDeleted()는 DELETED 상태일 때 true를 반환한다")
        void shouldReturnTrueWhenStatusIsDeleted() {
            // given
            ProductGroup productGroup = ProductGroupFixture.createDeleted();

            // then
            assertTrue(productGroup.isDeleted());
        }

        @Test
        @DisplayName("isSellable()은 ACTIVE 상태일 때 true를 반환한다")
        void shouldReturnTrueWhenSellable() {
            // given
            ProductGroup productGroup = ProductGroupFixture.create();

            // then
            assertTrue(productGroup.isSellable());
        }

        @Test
        @DisplayName("isSellable()은 INACTIVE 또는 DELETED 상태일 때 false를 반환한다")
        void shouldReturnFalseWhenNotSellable() {
            // given
            ProductGroup inactiveProductGroup = ProductGroupFixture.createInactive();
            ProductGroup deletedProductGroup = ProductGroupFixture.createDeleted();

            // then
            assertFalse(inactiveProductGroup.isSellable());
            assertFalse(deletedProductGroup.isSellable());
        }

        @Test
        @DisplayName("isEditable()은 DELETED가 아닌 상태일 때 true를 반환한다")
        void shouldReturnTrueWhenEditable() {
            // given
            ProductGroup activeProductGroup = ProductGroupFixture.create();
            ProductGroup inactiveProductGroup = ProductGroupFixture.createInactive();

            // then
            assertTrue(activeProductGroup.isEditable());
            assertTrue(inactiveProductGroup.isEditable());
        }

        @Test
        @DisplayName("isEditable()은 DELETED 상태일 때 false를 반환한다")
        void shouldReturnFalseWhenNotEditable() {
            // given
            ProductGroup deletedProductGroup = ProductGroupFixture.createDeleted();

            // then
            assertFalse(deletedProductGroup.isEditable());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID 값을 반환한다")
        void shouldReturnIdValue() {
            // given
            ProductGroup productGroup = ProductGroupFixture.createWithId(100L);

            // then
            assertEquals(100L, productGroup.getIdValue());
        }

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            ProductGroup productGroup = ProductGroupFixture.createNew();

            // then
            assertNull(productGroup.getIdValue());
            assertFalse(productGroup.hasId());
        }

        @Test
        @DisplayName("hasCustomShippingPolicy()는 배송정책 ID가 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasShippingPolicy() {
            // given
            ProductGroup productGroup = ProductGroupFixture.create();

            // then
            assertTrue(productGroup.hasCustomShippingPolicy());
        }

        @Test
        @DisplayName("hasCustomShippingPolicy()는 배송정책 ID가 없으면 false를 반환한다")
        void shouldReturnFalseWhenNoShippingPolicy() {
            // given
            ProductGroup productGroup = ProductGroupFixture.createWithoutPolicies();

            // then
            assertFalse(productGroup.hasCustomShippingPolicy());
        }

        @Test
        @DisplayName("hasCustomRefundPolicy()는 환불정책 ID가 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasRefundPolicy() {
            // given
            ProductGroup productGroup = ProductGroupFixture.create();

            // then
            assertTrue(productGroup.hasCustomRefundPolicy());
        }

        @Test
        @DisplayName("hasCustomRefundPolicy()는 환불정책 ID가 없으면 false를 반환한다")
        void shouldReturnFalseWhenNoRefundPolicy() {
            // given
            ProductGroup productGroup = ProductGroupFixture.createWithoutPolicies();

            // then
            assertFalse(productGroup.hasCustomRefundPolicy());
        }
    }
}

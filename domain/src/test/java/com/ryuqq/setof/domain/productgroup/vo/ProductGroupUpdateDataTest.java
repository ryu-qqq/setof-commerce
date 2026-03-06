package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupUpdateData Value Object 테스트")
class ProductGroupUpdateDataTest {

    private ProductGroupUpdateData defaultUpdateData() {
        return ProductGroupUpdateData.of(
                ProductGroupId.of(1L),
                ProductGroupName.of("수정된 상품그룹명"),
                BrandId.of(2L),
                CategoryId.of(3L),
                ShippingPolicyId.of(4L),
                RefundPolicyId.of(5L),
                OptionType.SINGLE,
                CommonVoFixtures.now());
    }

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("모든 필드로 ProductGroupUpdateData를 생성한다")
        void createWithAllFields() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            ProductGroupName name = ProductGroupName.of("수정된 상품그룹명");
            BrandId brandId = BrandId.of(2L);
            CategoryId categoryId = CategoryId.of(3L);
            ShippingPolicyId shippingPolicyId = ShippingPolicyId.of(4L);
            RefundPolicyId refundPolicyId = RefundPolicyId.of(5L);
            OptionType optionType = OptionType.SINGLE;
            Instant updatedAt = CommonVoFixtures.now();

            // when
            ProductGroupUpdateData updateData =
                    ProductGroupUpdateData.of(
                            productGroupId,
                            name,
                            brandId,
                            categoryId,
                            shippingPolicyId,
                            refundPolicyId,
                            optionType,
                            updatedAt);

            // then
            assertThat(updateData.productGroupId()).isEqualTo(productGroupId);
            assertThat(updateData.productGroupName()).isEqualTo(name);
            assertThat(updateData.brandId()).isEqualTo(brandId);
            assertThat(updateData.categoryId()).isEqualTo(categoryId);
            assertThat(updateData.shippingPolicyId()).isEqualTo(shippingPolicyId);
            assertThat(updateData.refundPolicyId()).isEqualTo(refundPolicyId);
            assertThat(updateData.optionType()).isEqualTo(optionType);
            assertThat(updateData.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("COMBINATION 옵션 타입으로 생성한다")
        void createWithCombinationOptionType() {
            // given
            Instant updatedAt = CommonVoFixtures.now();

            // when
            ProductGroupUpdateData updateData =
                    ProductGroupUpdateData.of(
                            ProductGroupId.of(1L),
                            ProductGroupName.of("조합 상품"),
                            BrandId.of(1L),
                            CategoryId.of(1L),
                            ShippingPolicyId.of(1L),
                            RefundPolicyId.of(1L),
                            OptionType.COMBINATION,
                            updatedAt);

            // then
            assertThat(updateData.optionType()).isEqualTo(OptionType.COMBINATION);
        }

        @Test
        @DisplayName("NONE 옵션 타입으로 생성한다")
        void createWithNoneOptionType() {
            // given
            Instant updatedAt = CommonVoFixtures.now();

            // when
            ProductGroupUpdateData updateData =
                    ProductGroupUpdateData.of(
                            ProductGroupId.of(1L),
                            ProductGroupName.of("단순 상품"),
                            BrandId.of(1L),
                            CategoryId.of(1L),
                            ShippingPolicyId.of(1L),
                            RefundPolicyId.of(1L),
                            OptionType.NONE,
                            updatedAt);

            // then
            assertThat(updateData.optionType()).isEqualTo(OptionType.NONE);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 ProductGroupUpdateData는 동등하다")
        void sameValueAreEqual() {
            // given
            Instant now = CommonVoFixtures.now();
            ProductGroupUpdateData updateData1 =
                    ProductGroupUpdateData.of(
                            ProductGroupId.of(1L),
                            ProductGroupName.of("상품그룹명"),
                            BrandId.of(2L),
                            CategoryId.of(3L),
                            ShippingPolicyId.of(4L),
                            RefundPolicyId.of(5L),
                            OptionType.SINGLE,
                            now);
            ProductGroupUpdateData updateData2 =
                    ProductGroupUpdateData.of(
                            ProductGroupId.of(1L),
                            ProductGroupName.of("상품그룹명"),
                            BrandId.of(2L),
                            CategoryId.of(3L),
                            ShippingPolicyId.of(4L),
                            RefundPolicyId.of(5L),
                            OptionType.SINGLE,
                            now);

            // then
            assertThat(updateData1).isEqualTo(updateData2);
        }
    }

    @Nested
    @DisplayName("Accessor Methods 테스트")
    class AccessorMethodTest {

        @Test
        @DisplayName("productGroupId()는 ProductGroupId를 반환한다")
        void returnsProductGroupId() {
            ProductGroupUpdateData updateData = defaultUpdateData();
            assertThat(updateData.productGroupId()).isEqualTo(ProductGroupId.of(1L));
        }

        @Test
        @DisplayName("productGroupName()은 ProductGroupName을 반환한다")
        void returnsProductGroupName() {
            ProductGroupUpdateData updateData = defaultUpdateData();
            assertThat(updateData.productGroupName()).isNotNull();
        }

        @Test
        @DisplayName("updatedAt()은 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            ProductGroupUpdateData updateData = defaultUpdateData();
            assertThat(updateData.updatedAt()).isNotNull();
        }
    }
}

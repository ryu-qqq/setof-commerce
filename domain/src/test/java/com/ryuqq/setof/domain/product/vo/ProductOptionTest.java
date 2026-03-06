package com.ryuqq.setof.domain.product.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.setof.commerce.domain.product.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductOption Value Object 테스트")
class ProductOptionTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 ProductOption을 생성한다")
        void createWithValidValues() {
            // given
            SellerOptionGroupId optionGroupId = SellerOptionGroupId.of(1L);
            SellerOptionValueId optionDetailId = SellerOptionValueId.of(2L);
            int sortOrder = 0;

            // when
            ProductOption productOption =
                    ProductOption.of(optionGroupId, optionDetailId, sortOrder);

            // then
            assertThat(productOption.optionGroupId()).isEqualTo(optionGroupId);
            assertThat(productOption.optionDetailId()).isEqualTo(optionDetailId);
            assertThat(productOption.sortOrder()).isEqualTo(sortOrder);
        }

        @Test
        @DisplayName("optionGroupId가 null이면 NullPointerException이 발생한다")
        void throwExceptionWhenSellerOptionGroupIdIsNull() {
            // when & then
            assertThatThrownBy(() -> ProductOption.of(null, SellerOptionValueId.of(1L), 0))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("옵션 그룹 ID는 필수입니다");
        }

        @Test
        @DisplayName("optionDetailId가 null이면 NullPointerException이 발생한다")
        void throwExceptionWhenSellerOptionValueIdIsNull() {
            // when & then
            assertThatThrownBy(() -> ProductOption.of(SellerOptionGroupId.of(1L), null, 0))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("옵션 상세 ID는 필수입니다");
        }

        @Test
        @DisplayName("sortOrder가 음수이면 IllegalArgumentException이 발생한다")
        void throwExceptionWhenSortOrderIsNegative() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    ProductOption.of(
                                            SellerOptionGroupId.of(1L),
                                            SellerOptionValueId.of(1L),
                                            -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("정렬 순서는 0 이상이어야 합니다");
        }

        @Test
        @DisplayName("sortOrder가 0이면 생성할 수 있다")
        void createWithZeroSortOrder() {
            // when
            ProductOption productOption =
                    ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(1L), 0);

            // then
            assertThat(productOption.sortOrder()).isZero();
        }

        @Test
        @DisplayName("sortOrder가 양수이면 생성할 수 있다")
        void createWithPositiveSortOrder() {
            // when
            ProductOption productOption =
                    ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(1L), 5);

            // then
            assertThat(productOption.sortOrder()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("optionGroupIdValue() - 옵션 그룹 ID 값 반환")
    class SellerOptionGroupIdValueTest {

        @Test
        @DisplayName("optionGroupIdValue()는 Long 값을 반환한다")
        void returnsSellerOptionGroupIdValue() {
            // given
            ProductOption productOption = ProductFixtures.defaultProductOption();

            // then
            assertThat(productOption.optionGroupIdValue()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("optionDetailIdValue() - 옵션 상세 ID 값 반환")
    class SellerOptionValueIdValueTest {

        @Test
        @DisplayName("optionDetailIdValue()는 Long 값을 반환한다")
        void returnsSellerOptionValueIdValue() {
            // given
            ProductOption productOption = ProductFixtures.defaultProductOption();

            // then
            assertThat(productOption.optionDetailIdValue()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ProductOption은 동일하다")
        void equalWithSameValues() {
            // given
            ProductOption option1 =
                    ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(2L), 0);
            ProductOption option2 =
                    ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(2L), 0);

            // then
            assertThat(option1).isEqualTo(option2);
            assertThat(option1.hashCode()).isEqualTo(option2.hashCode());
        }

        @Test
        @DisplayName("다른 optionGroupId를 가진 ProductOption은 동일하지 않다")
        void notEqualWithDifferentSellerOptionGroupId() {
            // given
            ProductOption option1 =
                    ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(1L), 0);
            ProductOption option2 =
                    ProductOption.of(SellerOptionGroupId.of(2L), SellerOptionValueId.of(1L), 0);

            // then
            assertThat(option1).isNotEqualTo(option2);
        }

        @Test
        @DisplayName("다른 sortOrder를 가진 ProductOption은 동일하지 않다")
        void notEqualWithDifferentSortOrder() {
            // given
            ProductOption option1 =
                    ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(1L), 0);
            ProductOption option2 =
                    ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(1L), 1);

            // then
            assertThat(option1).isNotEqualTo(option2);
        }
    }
}

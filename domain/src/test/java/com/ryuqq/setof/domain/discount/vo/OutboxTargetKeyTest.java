package com.ryuqq.setof.domain.discount.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OutboxTargetKey Value Object 테스트")
class OutboxTargetKeyTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 OutboxTargetKey를 생성한다")
        void createWithValidValues() {
            // when
            OutboxTargetKey key = OutboxTargetKey.of(DiscountTargetType.BRAND, 123L);

            // then
            assertThat(key.targetType()).isEqualTo(DiscountTargetType.BRAND);
            assertThat(key.targetId()).isEqualTo(123L);
        }

        @Test
        @DisplayName("targetType이 null이면 예외가 발생한다")
        void throwExceptionForNullTargetType() {
            // when & then
            assertThatThrownBy(() -> OutboxTargetKey.of(null, 100L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("타겟 유형");
        }

        @Test
        @DisplayName("targetId가 0이면 예외가 발생한다")
        void throwExceptionForZeroTargetId() {
            // when & then
            assertThatThrownBy(() -> OutboxTargetKey.of(DiscountTargetType.BRAND, 0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0보다");
        }

        @Test
        @DisplayName("targetId가 음수이면 예외가 발생한다")
        void throwExceptionForNegativeTargetId() {
            // when & then
            assertThatThrownBy(() -> OutboxTargetKey.of(DiscountTargetType.SELLER, -1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0보다");
        }
    }

    @Nested
    @DisplayName("toKey() - 문자열 변환 테스트")
    class ToKeyTest {

        @Test
        @DisplayName("BRAND:id 형식의 문자열을 반환한다")
        void toKeyReturnsBrandFormat() {
            // given
            OutboxTargetKey key = OutboxTargetKey.of(DiscountTargetType.BRAND, 123L);

            // when
            String result = key.toKey();

            // then
            assertThat(result).isEqualTo("BRAND:123");
        }

        @Test
        @DisplayName("SELLER:id 형식의 문자열을 반환한다")
        void toKeyReturnsSellerFormat() {
            // given
            OutboxTargetKey key = OutboxTargetKey.of(DiscountTargetType.SELLER, 45L);

            // then
            assertThat(key.toKey()).isEqualTo("SELLER:45");
        }

        @Test
        @DisplayName("PRODUCT:id 형식의 문자열을 반환한다")
        void toKeyReturnsProductFormat() {
            // given
            OutboxTargetKey key = OutboxTargetKey.of(DiscountTargetType.PRODUCT, 999L);

            // then
            assertThat(key.toKey()).isEqualTo("PRODUCT:999");
        }

        @Test
        @DisplayName("CATEGORY:id 형식의 문자열을 반환한다")
        void toKeyReturnsCategoryFormat() {
            // given
            OutboxTargetKey key = OutboxTargetKey.of(DiscountTargetType.CATEGORY, 777L);

            // then
            assertThat(key.toKey()).isEqualTo("CATEGORY:777");
        }
    }

    @Nested
    @DisplayName("fromKey() - 문자열 파싱 테스트")
    class FromKeyTest {

        @Test
        @DisplayName("'BRAND:123' 문자열을 파싱한다")
        void parsesBrandKey() {
            // when
            OutboxTargetKey key = OutboxTargetKey.fromKey("BRAND:123");

            // then
            assertThat(key.targetType()).isEqualTo(DiscountTargetType.BRAND);
            assertThat(key.targetId()).isEqualTo(123L);
        }

        @Test
        @DisplayName("'SELLER:45' 문자열을 파싱한다")
        void parsesSellerKey() {
            // when
            OutboxTargetKey key = OutboxTargetKey.fromKey("SELLER:45");

            // then
            assertThat(key.targetType()).isEqualTo(DiscountTargetType.SELLER);
            assertThat(key.targetId()).isEqualTo(45L);
        }

        @Test
        @DisplayName("잘못된 형식의 문자열이면 예외가 발생한다")
        void throwExceptionForInvalidFormat() {
            // when & then
            assertThatThrownBy(() -> OutboxTargetKey.fromKey("BRAND123"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("잘못된 타겟 키 형식");
        }

        @Test
        @DisplayName("존재하지 않는 타입이면 예외가 발생한다")
        void throwExceptionForUnknownType() {
            // when & then
            assertThatThrownBy(() -> OutboxTargetKey.fromKey("UNKNOWN:123"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("toKey()로 변환 후 fromKey()로 복원하면 동일한 객체를 얻는다")
        void roundTrip() {
            // given
            OutboxTargetKey original = OutboxTargetKey.of(DiscountTargetType.CATEGORY, 500L);

            // when
            OutboxTargetKey restored = OutboxTargetKey.fromKey(original.toKey());

            // then
            assertThat(restored).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 타입과 ID를 가진 OutboxTargetKey는 동등하다")
        void sameValuesAreEqual() {
            // given
            OutboxTargetKey key1 = OutboxTargetKey.of(DiscountTargetType.BRAND, 100L);
            OutboxTargetKey key2 = OutboxTargetKey.of(DiscountTargetType.BRAND, 100L);

            // then
            assertThat(key1).isEqualTo(key2);
            assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
        }

        @Test
        @DisplayName("다른 타입이면 동등하지 않다")
        void differentTypeNotEqual() {
            // given
            OutboxTargetKey key1 = OutboxTargetKey.of(DiscountTargetType.BRAND, 100L);
            OutboxTargetKey key2 = OutboxTargetKey.of(DiscountTargetType.SELLER, 100L);

            // then
            assertThat(key1).isNotEqualTo(key2);
        }

        @Test
        @DisplayName("다른 ID이면 동등하지 않다")
        void differentIdNotEqual() {
            // given
            OutboxTargetKey key1 = OutboxTargetKey.of(DiscountTargetType.BRAND, 100L);
            OutboxTargetKey key2 = OutboxTargetKey.of(DiscountTargetType.BRAND, 200L);

            // then
            assertThat(key1).isNotEqualTo(key2);
        }
    }
}

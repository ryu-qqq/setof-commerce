package com.ryuqq.setof.domain.shippingaddress.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddressSearchCondition 단위 테스트")
class ShippingAddressSearchConditionTest {

    @Nested
    @DisplayName("ofUserId() - 사용자 ID로 생성")
    class OfUserIdTest {

        @Test
        @DisplayName("ofUserId()로 userId만 있는 검색 조건을 생성한다")
        void createWithUserId() {
            // when
            ShippingAddressSearchCondition condition =
                    ShippingAddressSearchCondition.ofUserId(100L);

            // then
            assertThat(condition.userId()).isEqualTo(100L);
            assertThat(condition.shippingAddressId()).isNull();
            assertThat(condition.hasUserId()).isTrue();
            assertThat(condition.hasShippingAddressId()).isFalse();
        }
    }

    @Nested
    @DisplayName("of() - userId + shippingAddressId로 생성")
    class OfTest {

        @Test
        @DisplayName("of()로 userId와 shippingAddressId를 포함한 검색 조건을 생성한다")
        void createWithUserIdAndAddressId() {
            // when
            ShippingAddressSearchCondition condition =
                    ShippingAddressSearchCondition.of(100L, 200L);

            // then
            assertThat(condition.userId()).isEqualTo(100L);
            assertThat(condition.shippingAddressId()).isEqualTo(200L);
            assertThat(condition.hasUserId()).isTrue();
            assertThat(condition.hasShippingAddressId()).isTrue();
        }

        @Test
        @DisplayName("shippingAddressId가 null이면 hasShippingAddressId()는 false를 반환한다")
        void nullShippingAddressIdReturnsFalse() {
            // when
            ShippingAddressSearchCondition condition =
                    ShippingAddressSearchCondition.of(100L, null);

            // then
            assertThat(condition.hasShippingAddressId()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasUserId() / hasShippingAddressId() - 조건 존재 여부")
    class HasConditionTest {

        @Test
        @DisplayName("userId가 null이면 hasUserId()는 false를 반환한다")
        void nullUserIdReturnsFalse() {
            // when
            ShippingAddressSearchCondition condition =
                    ShippingAddressSearchCondition.of(null, 200L);

            // then
            assertThat(condition.hasUserId()).isFalse();
        }

        @Test
        @DisplayName("userId와 shippingAddressId 모두 null이면 두 조건 모두 false이다")
        void bothNullReturnsFalse() {
            // when
            ShippingAddressSearchCondition condition =
                    ShippingAddressSearchCondition.of(null, null);

            // then
            assertThat(condition.hasUserId()).isFalse();
            assertThat(condition.hasShippingAddressId()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값으로 생성한 두 검색 조건은 동등하다")
        void sameValuesAreEqual() {
            // given
            ShippingAddressSearchCondition cond1 = ShippingAddressSearchCondition.of(1L, 2L);
            ShippingAddressSearchCondition cond2 = ShippingAddressSearchCondition.of(1L, 2L);

            // then
            assertThat(cond1).isEqualTo(cond2);
            assertThat(cond1.hashCode()).isEqualTo(cond2.hashCode());
        }

        @Test
        @DisplayName("다른 값으로 생성한 두 검색 조건은 동등하지 않다")
        void differentValuesNotEquals() {
            // given
            ShippingAddressSearchCondition cond1 = ShippingAddressSearchCondition.ofUserId(1L);
            ShippingAddressSearchCondition cond2 = ShippingAddressSearchCondition.ofUserId(2L);

            // then
            assertThat(cond1).isNotEqualTo(cond2);
        }
    }
}

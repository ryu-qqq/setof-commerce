package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DisplayConfig Value Object 단위 테스트")
class DisplayConfigTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("모든 필드로 DisplayConfig를 생성한다")
        void createWithAllFields() {
            // when
            DisplayConfig config =
                    new DisplayConfig(ListType.TWO_STEP, OrderType.LOW_PRICE, BadgeType.NONE, true);

            // then
            assertThat(config.listType()).isEqualTo(ListType.TWO_STEP);
            assertThat(config.orderType()).isEqualTo(OrderType.LOW_PRICE);
            assertThat(config.badgeType()).isEqualTo(BadgeType.NONE);
            assertThat(config.filterEnabled()).isTrue();
        }

        @Test
        @DisplayName("filterEnabled가 false인 DisplayConfig를 생성한다")
        void createWithFilterDisabled() {
            // when
            DisplayConfig config =
                    new DisplayConfig(ListType.MULTI, OrderType.NONE, BadgeType.NONE, false);

            // then
            assertThat(config.filterEnabled()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값으로 생성된 DisplayConfig는 동등하다")
        void sameValuesAreEqual() {
            // given
            DisplayConfig config1 =
                    new DisplayConfig(ListType.TWO_STEP, OrderType.NONE, BadgeType.NONE, false);
            DisplayConfig config2 =
                    new DisplayConfig(ListType.TWO_STEP, OrderType.NONE, BadgeType.NONE, false);

            // then
            assertThat(config1).isEqualTo(config2);
            assertThat(config1.hashCode()).isEqualTo(config2.hashCode());
        }

        @Test
        @DisplayName("다른 OrderType을 가진 DisplayConfig는 동등하지 않다")
        void differentOrderTypeIsNotEqual() {
            // given
            DisplayConfig config1 =
                    new DisplayConfig(ListType.TWO_STEP, OrderType.NONE, BadgeType.NONE, false);
            DisplayConfig config2 =
                    new DisplayConfig(
                            ListType.TWO_STEP, OrderType.LOW_PRICE, BadgeType.NONE, false);

            // then
            assertThat(config1).isNotEqualTo(config2);
        }

        @Test
        @DisplayName("다른 filterEnabled 값을 가진 DisplayConfig는 동등하지 않다")
        void differentFilterEnabledIsNotEqual() {
            // given
            DisplayConfig config1 =
                    new DisplayConfig(ListType.TWO_STEP, OrderType.NONE, BadgeType.NONE, false);
            DisplayConfig config2 =
                    new DisplayConfig(ListType.TWO_STEP, OrderType.NONE, BadgeType.NONE, true);

            // then
            assertThat(config1).isNotEqualTo(config2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("record이므로 필드 접근은 메서드를 통해서만 가능하다")
        void fieldsAreAccessibleViaAccessorMethods() {
            // given
            DisplayConfig config =
                    new DisplayConfig(
                            ListType.TWO_STEP, OrderType.HIGH_PRICE, BadgeType.NONE, true);

            // then
            assertThat(config.listType()).isNotNull();
            assertThat(config.orderType()).isNotNull();
            assertThat(config.badgeType()).isNotNull();
        }
    }
}

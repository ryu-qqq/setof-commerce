package com.ryuqq.setof.domain.order.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("LegacyOrderId Value Object 단위 테스트")
class LegacyOrderIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 LegacyOrderId를 생성한다")
        void createWithOf() {
            // when
            LegacyOrderId id = LegacyOrderId.of(100L);

            // then
            assertThat(id.value()).isEqualTo(100L);
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void ofWithNullThrowsException() {
            assertThatThrownBy(() -> LegacyOrderId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }

        @Test
        @DisplayName("forNew()로 새 주문용 ID를 생성한다")
        void createWithForNew() {
            // when
            LegacyOrderId id = LegacyOrderId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            assertThat(LegacyOrderId.forNew().isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            assertThat(LegacyOrderId.of(1L).isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 LegacyOrderId는 동등하다")
        void sameValueEquals() {
            // given
            LegacyOrderId id1 = LegacyOrderId.of(100L);
            LegacyOrderId id2 = LegacyOrderId.of(100L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 LegacyOrderId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            LegacyOrderId id1 = LegacyOrderId.of(100L);
            LegacyOrderId id2 = LegacyOrderId.of(200L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}

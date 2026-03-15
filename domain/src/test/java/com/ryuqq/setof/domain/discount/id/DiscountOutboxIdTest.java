package com.ryuqq.setof.domain.discount.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountOutboxId Value Object 테스트")
class DiscountOutboxIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 DiscountOutboxId를 생성한다")
        void createWithOf() {
            // when
            DiscountOutboxId id = DiscountOutboxId.of(99L);

            // then
            assertThat(id.value()).isEqualTo(99L);
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void ofWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> DiscountOutboxId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }

        @Test
        @DisplayName("forNew()로 신규 ID를 생성한다")
        void createWithForNew() {
            // when
            DiscountOutboxId id = DiscountOutboxId.forNew();

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
            // given
            DiscountOutboxId id = DiscountOutboxId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            DiscountOutboxId id = DiscountOutboxId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 DiscountOutboxId는 동등하다")
        void sameValueEquals() {
            // given
            DiscountOutboxId id1 = DiscountOutboxId.of(500L);
            DiscountOutboxId id2 = DiscountOutboxId.of(500L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 DiscountOutboxId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            DiscountOutboxId id1 = DiscountOutboxId.of(500L);
            DiscountOutboxId id2 = DiscountOutboxId.of(600L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}

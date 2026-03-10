package com.ryuqq.setof.domain.qna.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("LegacyQnaId Value Object 단위 테스트")
class LegacyQnaIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 LegacyQnaId를 생성한다")
        void createWithOf() {
            // when
            LegacyQnaId legacyQnaId = LegacyQnaId.of(1001L);

            // then
            assertThat(legacyQnaId.value()).isEqualTo(1001L);
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void ofWithNullThrowsException() {
            assertThatThrownBy(() -> LegacyQnaId.of(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("forNew()로 새로운 LegacyQnaId를 생성한다")
        void createWithForNew() {
            // when
            LegacyQnaId legacyQnaId = LegacyQnaId.forNew();

            // then
            assertThat(legacyQnaId.value()).isNull();
            assertThat(legacyQnaId.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            assertThat(LegacyQnaId.forNew().isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            assertThat(LegacyQnaId.of(1001L).isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 LegacyQnaId는 동등하다")
        void sameValueEquals() {
            // given
            LegacyQnaId id1 = LegacyQnaId.of(1001L);
            LegacyQnaId id2 = LegacyQnaId.of(1001L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 LegacyQnaId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            LegacyQnaId id1 = LegacyQnaId.of(1001L);
            LegacyQnaId id2 = LegacyQnaId.of(1002L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}

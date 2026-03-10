package com.ryuqq.setof.domain.member.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("MemberAuthId Value Object 테스트")
class MemberAuthIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 MemberAuthId를 생성한다")
        void createWithOf() {
            // when
            MemberAuthId id = MemberAuthId.of(100L);

            // then
            assertThat(id.value()).isEqualTo(100L);
        }

        @Test
        @DisplayName("forNew()로 신규 MemberAuthId를 생성한다 (value null)")
        void createWithForNew() {
            // when
            MemberAuthId id = MemberAuthId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void createWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> MemberAuthId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            MemberAuthId id = MemberAuthId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            MemberAuthId id = MemberAuthId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 MemberAuthId는 동등하다")
        void sameValueEquals() {
            // given
            MemberAuthId id1 = MemberAuthId.of(100L);
            MemberAuthId id2 = MemberAuthId.of(100L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 MemberAuthId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            MemberAuthId id1 = MemberAuthId.of(100L);
            MemberAuthId id2 = MemberAuthId.of(200L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}

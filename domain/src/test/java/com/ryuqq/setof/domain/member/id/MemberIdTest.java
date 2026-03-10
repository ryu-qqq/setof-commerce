package com.ryuqq.setof.domain.member.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("MemberId Value Object 테스트")
class MemberIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 MemberId를 생성한다")
        void createWithOf() {
            // when
            MemberId memberId = MemberId.of("01900000-0000-7000-8000-000000000001");

            // then
            assertThat(memberId.value()).isEqualTo("01900000-0000-7000-8000-000000000001");
        }

        @Test
        @DisplayName("forNew()로 MemberId를 생성한다")
        void createWithForNew() {
            // when
            MemberId memberId = MemberId.forNew("01900000-0000-7000-8000-000000000002");

            // then
            assertThat(memberId.value()).isEqualTo("01900000-0000-7000-8000-000000000002");
        }

        @Test
        @DisplayName("null 값으로 생성하면 예외가 발생한다")
        void createWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> MemberId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }

        @Test
        @DisplayName("빈 문자열로 생성하면 예외가 발생한다")
        void createWithBlankThrowsException() {
            // when & then
            assertThatThrownBy(() -> MemberId.of("")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("공백 문자열로 생성하면 예외가 발생한다")
        void createWithWhitespaceThrowsException() {
            // when & then
            assertThatThrownBy(() -> MemberId.of("   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 MemberId는 동등하다")
        void sameValueEquals() {
            // given
            MemberId id1 = MemberId.of("01900000-0000-7000-8000-000000000001");
            MemberId id2 = MemberId.of("01900000-0000-7000-8000-000000000001");

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 MemberId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            MemberId id1 = MemberId.of("01900000-0000-7000-8000-000000000001");
            MemberId id2 = MemberId.of("01900000-0000-7000-8000-000000000002");

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}

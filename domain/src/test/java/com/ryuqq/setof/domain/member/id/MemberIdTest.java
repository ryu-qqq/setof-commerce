package com.ryuqq.setof.domain.member.id;

import static org.assertj.core.api.Assertions.assertThat;

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
            MemberId memberId = MemberId.of(1L);

            // then
            assertThat(memberId.value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("null 값으로 MemberId를 생성할 수 있다 (신규 회원용)")
        void createWithNull() {
            // when
            MemberId memberId = MemberId.of(null);

            // then
            assertThat(memberId.value()).isNull();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 MemberId는 동등하다")
        void sameValueEquals() {
            // given
            MemberId id1 = MemberId.of(1L);
            MemberId id2 = MemberId.of(1L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 MemberId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            MemberId id1 = MemberId.of(1L);
            MemberId id2 = MemberId.of(2L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}

package com.ryuqq.setof.domain.member.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.member.exception.InvalidMemberNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("MemberName Value Object")
class MemberNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class Creation {

        @ParameterizedTest
        @ValueSource(strings = {"홍길동", "김철수", "이영희", "박민수", "최지우"})
        @DisplayName("유효한 길이(2~5자)로 MemberName 생성 성공")
        void shouldCreateMemberNameWithValidLength(String name) {
            MemberName memberName = MemberName.of(name);

            assertNotNull(memberName);
            assertEquals(name, memberName.value());
        }

        @Test
        @DisplayName("2자 이름 생성 성공")
        void shouldCreateMemberNameWithMinLength() {
            MemberName memberName = MemberName.of("홍길");
            assertEquals("홍길", memberName.value());
        }

        @Test
        @DisplayName("5자 이름 생성 성공")
        void shouldCreateMemberNameWithMaxLength() {
            MemberName memberName = MemberName.of("홍길동이다");
            assertEquals("홍길동이다", memberName.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailure {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            assertThrows(InvalidMemberNameException.class, () -> MemberName.of(null));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            assertThrows(InvalidMemberNameException.class, () -> MemberName.of(""));
        }

        @Test
        @DisplayName("1자 이름으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsTooShort() {
            assertThrows(InvalidMemberNameException.class, () -> MemberName.of("홍"));
        }

        @Test
        @DisplayName("6자 이상 이름으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsTooLong() {
            assertThrows(InvalidMemberNameException.class, () -> MemberName.of("홍길동입니다"));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class Equality {

        @Test
        @DisplayName("같은 이름을 가진 MemberName은 동등")
        void shouldBeEqualWhenSameName() {
            MemberName name1 = MemberName.of("홍길동");
            MemberName name2 = MemberName.of("홍길동");

            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }
    }
}

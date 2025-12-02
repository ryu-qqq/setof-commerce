package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidSocialIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SocialId Value Object")
class SocialIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class Creation {

        @Test
        @DisplayName("유효한 값으로 SocialId 생성 성공")
        void shouldCreateSocialIdWithValidValue() {
            SocialId socialId = SocialId.of("kakao_123456789");

            assertNotNull(socialId);
            assertEquals("kakao_123456789", socialId.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailure {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenSocialIdIsNull() {
            assertThrows(InvalidSocialIdException.class, () -> SocialId.of(null));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenSocialIdIsEmpty() {
            assertThrows(InvalidSocialIdException.class, () -> SocialId.of(""));
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenSocialIdIsBlank(String blank) {
            assertThrows(InvalidSocialIdException.class, () -> SocialId.of(blank));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class Equality {

        @Test
        @DisplayName("같은 값을 가진 SocialId는 동등")
        void shouldBeEqualWhenSameValue() {
            SocialId id1 = SocialId.of("kakao_123");
            SocialId id2 = SocialId.of("kakao_123");

            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }
    }
}

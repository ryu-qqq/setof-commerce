package com.ryuqq.setof.domain.core.member.vo;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.exception.InvalidMemberIdException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * MemberId Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - UUID 검증
 */
@DisplayName("MemberId Value Object")
class MemberIdTest {

    private static final String VALID_UUID = "01234567-89ab-7cde-8000-000000000001";
    private static final UUID VALID_UUID_OBJECT = UUID.fromString(VALID_UUID);

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 UUID로 MemberId 생성")
        void shouldCreateMemberIdWithValidUuid() {
            // When
            MemberId memberId = MemberId.of(VALID_UUID_OBJECT);

            // Then
            assertNotNull(memberId);
            assertEquals(VALID_UUID_OBJECT, memberId.value());
        }

        @Test
        @DisplayName("유효한 UUID 문자열로 MemberId 생성")
        void shouldCreateMemberIdWithValidUuidString() {
            // When
            MemberId memberId = MemberId.of(VALID_UUID);

            // Then
            assertNotNull(memberId);
            assertEquals(VALID_UUID, memberId.asString());
        }

        @Test
        @DisplayName("generate()로 새 UUID v7 MemberId 생성")
        void shouldGenerateNewMemberId() {
            // When
            MemberId memberId = MemberId.generate();

            // Then
            assertNotNull(memberId);
            assertNotNull(memberId.value());

            // UUID v7 version 확인 (version bits = 0111 = 7)
            int version = memberId.value().version();
            assertEquals(7, version, "UUID v7 버전이어야 합니다");
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null UUID로 생성 시 예외 발생")
        void shouldThrowExceptionWhenUuidIsNull() {
            // Given
            UUID nullUuid = null;

            // When & Then
            InvalidMemberIdException exception =
                    assertThrows(InvalidMemberIdException.class, () -> MemberId.of(nullUuid));

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("null 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenStringIsNull() {
            // Given
            String nullString = null;

            // When & Then
            InvalidMemberIdException exception =
                    assertThrows(InvalidMemberIdException.class, () -> MemberId.of(nullString));

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenStringIsEmpty() {
            // Given
            String emptyString = "";

            // When & Then
            InvalidMemberIdException exception =
                    assertThrows(InvalidMemberIdException.class, () -> MemberId.of(emptyString));

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("잘못된 UUID 형식으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenInvalidUuidFormat() {
            // Given
            String invalidUuid = "not-a-valid-uuid";

            // When & Then
            InvalidMemberIdException exception =
                    assertThrows(InvalidMemberIdException.class, () -> MemberId.of(invalidUuid));

            assertNotNull(exception.getMessage());
            assertTrue(exception.getMessage().contains(invalidUuid));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 UUID를 가진 MemberId는 동등하다")
        void shouldBeEqualWhenSameUuid() {
            // Given
            MemberId memberId1 = MemberId.of(VALID_UUID);
            MemberId memberId2 = MemberId.of(VALID_UUID);

            // When & Then
            assertEquals(memberId1, memberId2);
            assertEquals(memberId1.hashCode(), memberId2.hashCode());
        }

        @Test
        @DisplayName("다른 UUID를 가진 MemberId는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentUuid() {
            // Given
            MemberId memberId1 = MemberId.of("01234567-89ab-7cde-8000-000000000001");
            MemberId memberId2 = MemberId.of("01234567-89ab-7cde-8000-000000000002");

            // When & Then
            assertNotEquals(memberId1, memberId2);
        }
    }

    @Nested
    @DisplayName("문자열 변환 테스트")
    class StringConversionTest {

        @Test
        @DisplayName("asString()은 하이픈 포함 UUID 반환")
        void shouldReturnUuidStringWithHyphens() {
            // Given
            MemberId memberId = MemberId.of(VALID_UUID);

            // When
            String result = memberId.asString();

            // Then
            assertEquals(VALID_UUID, result);
            assertTrue(result.contains("-"));
        }

        @Test
        @DisplayName("asCompactString()은 하이픈 없는 UUID 반환")
        void shouldReturnUuidStringWithoutHyphens() {
            // Given
            MemberId memberId = MemberId.of(VALID_UUID);

            // When
            String result = memberId.asCompactString();

            // Then
            assertFalse(result.contains("-"));
            assertEquals(VALID_UUID.replace("-", ""), result);
        }
    }
}

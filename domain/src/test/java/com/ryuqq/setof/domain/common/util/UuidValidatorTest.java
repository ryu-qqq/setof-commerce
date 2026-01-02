package com.ryuqq.setof.domain.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("UuidValidator 테스트")
class UuidValidatorTest {

    @Nested
    @DisplayName("isValid 메서드")
    class IsValidTest {

        @Test
        @DisplayName("하이픈 포함 UUID는 유효함")
        void shouldReturnTrueForValidUuidWithHyphens() {
            // given
            String validUuid = "550e8400-e29b-41d4-a716-446655440000";

            // when & then
            assertThat(UuidValidator.isValid(validUuid)).isTrue();
        }

        @Test
        @DisplayName("하이픈 미포함 UUID는 유효함")
        void shouldReturnTrueForValidUuidWithoutHyphens() {
            // given
            String validUuid = "550e8400e29b41d4a716446655440000";

            // when & then
            assertThat(UuidValidator.isValid(validUuid)).isTrue();
        }

        @Test
        @DisplayName("대소문자 혼합 UUID는 유효함")
        void shouldReturnTrueForMixedCaseUuid() {
            // given
            String validUuid = "550E8400-E29B-41D4-A716-446655440000";

            // when & then
            assertThat(UuidValidator.isValid(validUuid)).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 문자열은 유효하지 않음")
        void shouldReturnFalseForNullOrEmpty(String value) {
            assertThat(UuidValidator.isValid(value)).isFalse();
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "invalid-uuid",
                    "123",
                    "550e8400-e29b-41d4-a716",
                    "550e8400-e29b-41d4-a716-44665544000g",
                    "gggggggg-gggg-gggg-gggg-gggggggggggg"
                })
        @DisplayName("잘못된 형식은 유효하지 않음")
        void shouldReturnFalseForInvalidFormat(String value) {
            assertThat(UuidValidator.isValid(value)).isFalse();
        }
    }

    @Nested
    @DisplayName("requireValid 메서드")
    class RequireValidTest {

        @Test
        @DisplayName("유효한 UUID는 예외를 발생시키지 않음")
        void shouldNotThrowForValidUuid() {
            // given
            String validUuid = "550e8400-e29b-41d4-a716-446655440000";

            // when & then - 예외 없음
            UuidValidator.requireValid(validUuid, "회원 ID");
        }

        @Test
        @DisplayName("null UUID는 예외를 발생시킴")
        void shouldThrowForNullUuid() {
            assertThatThrownBy(() -> UuidValidator.requireValid(null, "회원 ID"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("회원 ID")
                    .hasMessageContaining("유효한 UUID 형식");
        }

        @Test
        @DisplayName("잘못된 형식 UUID는 예외를 발생시킴")
        void shouldThrowForInvalidUuid() {
            // given
            String invalidUuid = "invalid-uuid";

            // when & then
            assertThatThrownBy(() -> UuidValidator.requireValid(invalidUuid, "주문 ID"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 ID")
                    .hasMessageContaining("유효한 UUID 형식")
                    .hasMessageContaining("invalid-uuid");
        }
    }

    @Nested
    @DisplayName("normalizeUuidString 메서드")
    class NormalizeUuidStringTest {

        @Test
        @DisplayName("32자 UUID를 36자로 정규화함")
        void shouldNormalize32CharUuidTo36Char() {
            // given
            String compactUuid = "550e8400e29b41d4a716446655440000";

            // when
            String normalized = UuidValidator.normalizeUuidString(compactUuid);

            // then
            assertThat(normalized).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        }

        @Test
        @DisplayName("36자 UUID는 그대로 반환함")
        void shouldReturnSameFor36CharUuid() {
            // given
            String normalUuid = "550e8400-e29b-41d4-a716-446655440000";

            // when
            String normalized = UuidValidator.normalizeUuidString(normalUuid);

            // then
            assertThat(normalized).isEqualTo(normalUuid);
        }

        @Test
        @DisplayName("null은 null을 반환함")
        void shouldReturnNullForNull() {
            assertThat(UuidValidator.normalizeUuidString(null)).isNull();
        }
    }
}

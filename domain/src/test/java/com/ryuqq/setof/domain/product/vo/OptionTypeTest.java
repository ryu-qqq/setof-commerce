package com.ryuqq.setof.domain.product.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * OptionType Enum 테스트
 *
 * <p>옵션 타입에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("OptionType Enum")
class OptionTypeTest {

    @Nested
    @DisplayName("displayName() - 표시명 반환")
    class DisplayNameTest {

        @Test
        @DisplayName("SINGLE의 표시명은 '단품'이다")
        void shouldReturnCorrectDisplayNameForSingle() {
            // When & Then
            assertEquals("단품", OptionType.SINGLE.displayName());
        }

        @Test
        @DisplayName("ONE_LEVEL의 표시명은 '1단 옵션'이다")
        void shouldReturnCorrectDisplayNameForOneLevel() {
            // When & Then
            assertEquals("1단 옵션", OptionType.ONE_LEVEL.displayName());
        }

        @Test
        @DisplayName("TWO_LEVEL의 표시명은 '2단 옵션'이다")
        void shouldReturnCorrectDisplayNameForTwoLevel() {
            // When & Then
            assertEquals("2단 옵션", OptionType.TWO_LEVEL.displayName());
        }
    }

    @Nested
    @DisplayName("hasOption() - 옵션 존재 여부")
    class HasOptionTest {

        @Test
        @DisplayName("SINGLE은 옵션이 없다")
        void shouldReturnFalseForSingle() {
            // When & Then
            assertFalse(OptionType.SINGLE.hasOption());
        }

        @Test
        @DisplayName("ONE_LEVEL은 옵션이 있다")
        void shouldReturnTrueForOneLevel() {
            // When & Then
            assertTrue(OptionType.ONE_LEVEL.hasOption());
        }

        @Test
        @DisplayName("TWO_LEVEL은 옵션이 있다")
        void shouldReturnTrueForTwoLevel() {
            // When & Then
            assertTrue(OptionType.TWO_LEVEL.hasOption());
        }
    }

    @Nested
    @DisplayName("isTwoLevel() - 2단 옵션 여부")
    class IsTwoLevelTest {

        @Test
        @DisplayName("SINGLE은 2단 옵션이 아니다")
        void shouldReturnFalseForSingle() {
            // When & Then
            assertFalse(OptionType.SINGLE.isTwoLevel());
        }

        @Test
        @DisplayName("ONE_LEVEL은 2단 옵션이 아니다")
        void shouldReturnFalseForOneLevel() {
            // When & Then
            assertFalse(OptionType.ONE_LEVEL.isTwoLevel());
        }

        @Test
        @DisplayName("TWO_LEVEL은 2단 옵션이다")
        void shouldReturnTrueForTwoLevel() {
            // When & Then
            assertTrue(OptionType.TWO_LEVEL.isTwoLevel());
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("OptionType은 3개의 값을 가진다")
        void shouldHaveThreeValues() {
            // When & Then
            assertEquals(3, OptionType.values().length);
        }

        @Test
        @DisplayName("각 Enum 값이 올바르게 정의되어 있다")
        void shouldHaveCorrectValues() {
            // When & Then
            assertEquals("SINGLE", OptionType.SINGLE.name());
            assertEquals("ONE_LEVEL", OptionType.ONE_LEVEL.name());
            assertEquals("TWO_LEVEL", OptionType.TWO_LEVEL.name());
        }
    }
}

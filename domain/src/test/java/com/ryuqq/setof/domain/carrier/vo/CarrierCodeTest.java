package com.ryuqq.setof.domain.carrier.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.carrier.exception.InvalidCarrierCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** CarrierCode Value Object 테스트 */
@DisplayName("CarrierCode Value Object")
class CarrierCodeTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("유효한 코드로 CarrierCode를 생성할 수 있다")
        void shouldCreateWithValidCode() {
            // when
            CarrierCode code = CarrierCode.of("04");

            // then
            assertNotNull(code);
            assertEquals("04", code.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {"04", "05", "08", "01", "MANUAL"})
        @DisplayName("다양한 택배사 코드로 생성할 수 있다")
        void shouldCreateWithVariousCodes(String value) {
            // when
            CarrierCode code = CarrierCode.of(value);

            // then
            assertEquals(value, code.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 케이스")
    class ValidationFailure {

        @Test
        @DisplayName("null 코드로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNullCode() {
            // when & then
            assertThrows(InvalidCarrierCodeException.class, () -> CarrierCode.of(null));
        }

        @Test
        @DisplayName("빈 코드로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForEmptyCode() {
            // when & then
            assertThrows(InvalidCarrierCodeException.class, () -> CarrierCode.of(""));
        }

        @Test
        @DisplayName("공백만 있는 코드로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForBlankCode() {
            // when & then
            assertThrows(InvalidCarrierCodeException.class, () -> CarrierCode.of("   "));
        }

        @Test
        @DisplayName("최대 길이를 초과하는 코드로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForTooLongCode() {
            // given
            String tooLongCode = "12345678901"; // 11자 (최대 10자)

            // when & then
            assertThrows(InvalidCarrierCodeException.class, () -> CarrierCode.of(tooLongCode));
        }
    }
}

package com.ryuqq.setof.domain.carrier.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.carrier.exception.InvalidCarrierNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** CarrierName Value Object 테스트 */
@DisplayName("CarrierName Value Object")
class CarrierNameTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("유효한 이름으로 CarrierName을 생성할 수 있다")
        void shouldCreateWithValidName() {
            // when
            CarrierName name = CarrierName.of("CJ대한통운");

            // then
            assertNotNull(name);
            assertEquals("CJ대한통운", name.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {"CJ대한통운", "한진택배", "롯데택배", "우체국", "로젠택배"})
        @DisplayName("다양한 택배사명으로 생성할 수 있다")
        void shouldCreateWithVariousNames(String value) {
            // when
            CarrierName name = CarrierName.of(value);

            // then
            assertEquals(value, name.value());
        }

        @Test
        @DisplayName("최대 길이(50자)까지 생성할 수 있다")
        void shouldCreateWithMaxLength() {
            // given
            String maxLengthName = "a".repeat(50);

            // when
            CarrierName name = CarrierName.of(maxLengthName);

            // then
            assertEquals(50, name.value().length());
        }
    }

    @Nested
    @DisplayName("검증 실패 케이스")
    class ValidationFailure {

        @Test
        @DisplayName("null 이름으로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNullName() {
            // when & then
            assertThrows(InvalidCarrierNameException.class, () -> CarrierName.of(null));
        }

        @Test
        @DisplayName("빈 이름으로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForEmptyName() {
            // when & then
            assertThrows(InvalidCarrierNameException.class, () -> CarrierName.of(""));
        }

        @Test
        @DisplayName("공백만 있는 이름으로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForBlankName() {
            // when & then
            assertThrows(InvalidCarrierNameException.class, () -> CarrierName.of("   "));
        }

        @Test
        @DisplayName("최대 길이를 초과하는 이름으로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForTooLongName() {
            // given
            String tooLongName = "a".repeat(51); // 51자 (최대 50자)

            // when & then
            assertThrows(InvalidCarrierNameException.class, () -> CarrierName.of(tooLongName));
        }
    }
}

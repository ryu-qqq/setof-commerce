package com.ryuqq.setof.domain.carrier.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.carrier.exception.InvalidCarrierIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** CarrierId Value Object 테스트 */
@DisplayName("CarrierId Value Object")
class CarrierIdTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("유효한 ID로 CarrierId를 생성할 수 있다")
        void shouldCreateWithValidId() {
            // when
            CarrierId carrierId = CarrierId.of(1L);

            // then
            assertNotNull(carrierId);
            assertEquals(1L, carrierId.value());
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 100L, Long.MAX_VALUE})
        @DisplayName("양수 ID로 CarrierId를 생성할 수 있다")
        void shouldCreateWithPositiveId(Long value) {
            // when
            CarrierId carrierId = CarrierId.of(value);

            // then
            assertEquals(value, carrierId.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 케이스")
    class ValidationFailure {

        @Test
        @DisplayName("null ID로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNullId() {
            // when & then
            assertThrows(InvalidCarrierIdException.class, () -> CarrierId.of(null));
        }

        @Test
        @DisplayName("0 ID로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForZeroId() {
            // when & then
            assertThrows(InvalidCarrierIdException.class, () -> CarrierId.of(0L));
        }

        @Test
        @DisplayName("음수 ID로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNegativeId() {
            // when & then
            assertThrows(InvalidCarrierIdException.class, () -> CarrierId.of(-1L));
        }
    }
}

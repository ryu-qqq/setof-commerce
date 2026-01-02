package com.ryuqq.setof.domain.shipment.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.shipment.exception.InvalidShipmentIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** ShipmentId Value Object 테스트 */
@DisplayName("ShipmentId Value Object")
class ShipmentIdTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("유효한 ID로 ShipmentId를 생성할 수 있다")
        void shouldCreateWithValidId() {
            // when
            ShipmentId shipmentId = ShipmentId.of(1L);

            // then
            assertNotNull(shipmentId);
            assertEquals(1L, shipmentId.value());
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 100L, Long.MAX_VALUE})
        @DisplayName("양수 ID로 ShipmentId를 생성할 수 있다")
        void shouldCreateWithPositiveId(Long value) {
            // when
            ShipmentId shipmentId = ShipmentId.of(value);

            // then
            assertEquals(value, shipmentId.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 케이스")
    class ValidationFailure {

        @Test
        @DisplayName("null ID로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNullId() {
            // when & then
            assertThrows(InvalidShipmentIdException.class, () -> ShipmentId.of(null));
        }

        @Test
        @DisplayName("0 ID로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForZeroId() {
            // when & then
            assertThrows(InvalidShipmentIdException.class, () -> ShipmentId.of(0L));
        }

        @Test
        @DisplayName("음수 ID로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNegativeId() {
            // when & then
            assertThrows(InvalidShipmentIdException.class, () -> ShipmentId.of(-1L));
        }
    }
}

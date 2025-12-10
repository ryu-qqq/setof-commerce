package com.ryuqq.setof.domain.shippingaddress.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidAddressNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** AddressName Value Object 테스트 */
@DisplayName("AddressName Value Object")
class AddressNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 배송지 이름으로 AddressName 생성")
        void shouldCreateAddressNameWithValidValue() {
            // Given
            String validName = "집";

            // When
            AddressName addressName = AddressName.of(validName);

            // Then
            assertNotNull(addressName);
            assertEquals(validName, addressName.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 배송지 이름으로 생성")
        @ValueSource(strings = {"집", "회사", "부모님 댁", "본가", "친구집"})
        void shouldCreateAddressNameWithVariousValidValues(String validName) {
            // When
            AddressName addressName = AddressName.of(validName);

            // Then
            assertNotNull(addressName);
            assertEquals(validName, addressName.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            // When & Then
            assertThrows(InvalidAddressNameException.class, () -> AddressName.of(null));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // When & Then
            assertThrows(InvalidAddressNameException.class, () -> AddressName.of(""));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // When & Then
            assertThrows(InvalidAddressNameException.class, () -> AddressName.of("   "));
        }

        @Test
        @DisplayName("30자를 초과하는 배송지 이름으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String longName = "가".repeat(31);

            // When & Then
            assertThrows(InvalidAddressNameException.class, () -> AddressName.of(longName));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 AddressName은 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            AddressName name1 = AddressName.of("집");
            AddressName name2 = AddressName.of("집");

            // When & Then
            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 AddressName은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            AddressName name1 = AddressName.of("집");
            AddressName name2 = AddressName.of("회사");

            // When & Then
            assertNotEquals(name1, name2);
        }
    }
}

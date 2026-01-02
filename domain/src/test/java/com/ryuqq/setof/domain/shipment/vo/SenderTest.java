package com.ryuqq.setof.domain.shipment.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.shipment.exception.InvalidSenderInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Sender Value Object 테스트 */
@DisplayName("Sender Value Object")
class SenderTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("전체 정보로 Sender를 생성할 수 있다")
        void shouldCreateWithFullInfo() {
            // when
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구 테헤란로 123");

            // then
            assertNotNull(sender);
            assertEquals("테스트셀러", sender.name());
            assertEquals("010-1234-5678", sender.phone());
            assertEquals("서울시 강남구 테헤란로 123", sender.address());
            assertTrue(sender.hasAddress());
        }

        @Test
        @DisplayName("주소 없이 Sender를 생성할 수 있다")
        void shouldCreateWithoutAddress() {
            // when
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", null);

            // then
            assertNotNull(sender);
            assertFalse(sender.hasAddress());
        }
    }

    @Nested
    @DisplayName("hasAddress() - 주소 존재 여부")
    class HasAddress {

        @Test
        @DisplayName("주소가 있으면 true를 반환한다")
        void shouldReturnTrueWhenAddressExists() {
            // given
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구");

            // when & then
            assertTrue(sender.hasAddress());
        }

        @Test
        @DisplayName("주소가 null이면 false를 반환한다")
        void shouldReturnFalseWhenAddressIsNull() {
            // given
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", null);

            // when & then
            assertFalse(sender.hasAddress());
        }

        @Test
        @DisplayName("주소가 빈 문자열이면 false를 반환한다")
        void shouldReturnFalseWhenAddressIsBlank() {
            // given
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", "   ");

            // when & then
            assertFalse(sender.hasAddress());
        }
    }

    @Nested
    @DisplayName("검증 실패 케이스")
    class ValidationFailure {

        @Test
        @DisplayName("null 이름으로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNullName() {
            // when & then
            assertThrows(
                    InvalidSenderInfoException.class, () -> Sender.of(null, "010-1234-5678", "주소"));
        }

        @Test
        @DisplayName("빈 이름으로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForEmptyName() {
            // when & then
            assertThrows(
                    InvalidSenderInfoException.class, () -> Sender.of("", "010-1234-5678", "주소"));
        }

        @Test
        @DisplayName("공백만 있는 이름으로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForBlankName() {
            // when & then
            assertThrows(
                    InvalidSenderInfoException.class,
                    () -> Sender.of("   ", "010-1234-5678", "주소"));
        }

        @Test
        @DisplayName("이름이 최대 길이를 초과하면 예외가 발생한다")
        void shouldThrowExceptionForTooLongName() {
            // given
            String tooLongName = "a".repeat(51); // 51자 (최대 50자)

            // when & then
            assertThrows(
                    InvalidSenderInfoException.class,
                    () -> Sender.of(tooLongName, "010-1234-5678", "주소"));
        }

        @Test
        @DisplayName("null 연락처로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNullPhone() {
            // when & then
            assertThrows(InvalidSenderInfoException.class, () -> Sender.of("테스트", null, "주소"));
        }

        @Test
        @DisplayName("빈 연락처로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForEmptyPhone() {
            // when & then
            assertThrows(InvalidSenderInfoException.class, () -> Sender.of("테스트", "", "주소"));
        }

        @Test
        @DisplayName("연락처가 최대 길이를 초과하면 예외가 발생한다")
        void shouldThrowExceptionForTooLongPhone() {
            // given
            String tooLongPhone = "0".repeat(21); // 21자 (최대 20자)

            // when & then
            assertThrows(
                    InvalidSenderInfoException.class, () -> Sender.of("테스트", tooLongPhone, "주소"));
        }

        @Test
        @DisplayName("주소가 최대 길이를 초과하면 예외가 발생한다")
        void shouldThrowExceptionForTooLongAddress() {
            // given
            String tooLongAddress = "a".repeat(201); // 201자 (최대 200자)

            // when & then
            assertThrows(
                    InvalidSenderInfoException.class,
                    () -> Sender.of("테스트", "010-1234-5678", tooLongAddress));
        }
    }
}

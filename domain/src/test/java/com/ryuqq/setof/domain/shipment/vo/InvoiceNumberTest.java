package com.ryuqq.setof.domain.shipment.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.shipment.exception.InvalidInvoiceNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** InvoiceNumber Value Object 테스트 */
@DisplayName("InvoiceNumber Value Object")
class InvoiceNumberTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("유효한 번호로 InvoiceNumber를 생성할 수 있다")
        void shouldCreateWithValidNumber() {
            // when
            InvoiceNumber invoice = InvoiceNumber.of("1234567890");

            // then
            assertNotNull(invoice);
            assertEquals("1234567890", invoice.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {"123456789012", "CJ-12345-67890", "abc123", "123-456-789012"})
        @DisplayName("다양한 형식의 운송장 번호로 생성할 수 있다")
        void shouldCreateWithVariousFormats(String value) {
            // when
            InvoiceNumber invoice = InvoiceNumber.of(value);

            // then
            assertEquals(value, invoice.value());
        }

        @Test
        @DisplayName("최대 길이(30자)까지 생성할 수 있다")
        void shouldCreateWithMaxLength() {
            // given
            String maxLengthNumber = "a".repeat(30);

            // when
            InvoiceNumber invoice = InvoiceNumber.of(maxLengthNumber);

            // then
            assertEquals(30, invoice.value().length());
        }
    }

    @Nested
    @DisplayName("withoutHyphen() - 하이픈 제거")
    class WithoutHyphen {

        @Test
        @DisplayName("하이픈이 포함된 운송장 번호에서 하이픈을 제거할 수 있다")
        void shouldRemoveHyphens() {
            // given
            InvoiceNumber invoice = InvoiceNumber.of("123-456-789012");

            // when
            String result = invoice.withoutHyphen();

            // then
            assertEquals("123456789012", result);
        }

        @Test
        @DisplayName("하이픈이 없는 운송장 번호는 그대로 반환한다")
        void shouldReturnSameWhenNoHyphens() {
            // given
            InvoiceNumber invoice = InvoiceNumber.of("123456789012");

            // when
            String result = invoice.withoutHyphen();

            // then
            assertEquals("123456789012", result);
        }
    }

    @Nested
    @DisplayName("검증 실패 케이스")
    class ValidationFailure {

        @Test
        @DisplayName("null 번호로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForNullNumber() {
            // when & then
            assertThrows(InvalidInvoiceNumberException.class, () -> InvoiceNumber.of(null));
        }

        @Test
        @DisplayName("빈 번호로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForEmptyNumber() {
            // when & then
            assertThrows(InvalidInvoiceNumberException.class, () -> InvoiceNumber.of(""));
        }

        @Test
        @DisplayName("공백만 있는 번호로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForBlankNumber() {
            // when & then
            assertThrows(InvalidInvoiceNumberException.class, () -> InvoiceNumber.of("   "));
        }

        @Test
        @DisplayName("최대 길이를 초과하는 번호로 생성하면 예외가 발생한다")
        void shouldThrowExceptionForTooLongNumber() {
            // given
            String tooLongNumber = "a".repeat(31); // 31자 (최대 30자)

            // when & then
            assertThrows(
                    InvalidInvoiceNumberException.class, () -> InvoiceNumber.of(tooLongNumber));
        }
    }
}

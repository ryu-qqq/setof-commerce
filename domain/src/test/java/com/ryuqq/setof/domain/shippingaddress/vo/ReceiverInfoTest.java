package com.ryuqq.setof.domain.shippingaddress.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidReceiverInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * ReceiverInfo Value Object 테스트
 *
 * <p>수령인 정보 (이름 + 연락처) 복합 VO를 테스트합니다.
 */
@DisplayName("ReceiverInfo Value Object")
class ReceiverInfoTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 정보로 ReceiverInfo 생성")
        void shouldCreateReceiverInfoWithValidValues() {
            // Given
            String name = "홍길동";
            String phone = "01012345678";

            // When
            ReceiverInfo receiverInfo = ReceiverInfo.of(name, phone);

            // Then
            assertNotNull(receiverInfo);
            assertEquals(name, receiverInfo.name());
            assertEquals(phone, receiverInfo.phone());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 전화번호로 생성")
        @ValueSource(strings = {"01012345678", "010-1234-5678", "02-1234-5678", "0312345678"})
        void shouldCreateReceiverInfoWithVariousPhoneFormats(String phone) {
            // When
            ReceiverInfo receiverInfo = ReceiverInfo.of("홍길동", phone);

            // Then
            assertNotNull(receiverInfo);
            assertEquals(phone, receiverInfo.phone());
        }
    }

    @Nested
    @DisplayName("이름 검증 실패 테스트")
    class NameValidationFailureTest {

        @Test
        @DisplayName("이름이 null이면 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of(null, "01012345678"));
        }

        @Test
        @DisplayName("이름이 빈 문자열이면 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of("", "01012345678"));
        }

        @Test
        @DisplayName("이름이 공백이면 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of("   ", "01012345678"));
        }

        @Test
        @DisplayName("이름이 20자를 초과하면 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String longName = "가".repeat(21);

            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of(longName, "01012345678"));
        }
    }

    @Nested
    @DisplayName("전화번호 검증 실패 테스트")
    class PhoneValidationFailureTest {

        @Test
        @DisplayName("전화번호가 null이면 예외 발생")
        void shouldThrowExceptionWhenPhoneIsNull() {
            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of("홍길동", null));
        }

        @Test
        @DisplayName("전화번호가 빈 문자열이면 예외 발생")
        void shouldThrowExceptionWhenPhoneIsEmpty() {
            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of("홍길동", ""));
        }

        @Test
        @DisplayName("전화번호가 10자리 미만이면 예외 발생")
        void shouldThrowExceptionWhenPhoneIsTooShort() {
            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of("홍길동", "123456789"));
        }

        @Test
        @DisplayName("전화번호가 15자리를 초과하면 예외 발생")
        void shouldThrowExceptionWhenPhoneIsTooLong() {
            // Given
            String longPhone = "1234567890123456"; // 16자리

            // When & Then
            assertThrows(InvalidReceiverInfoException.class, () -> ReceiverInfo.of("홍길동", longPhone));
        }
    }

    @Nested
    @DisplayName("유틸리티 메서드 테스트")
    class UtilityMethodTest {

        @Test
        @DisplayName("normalizedPhone()은 숫자만 반환한다")
        void shouldReturnNormalizedPhone() {
            // Given
            ReceiverInfo receiverInfo = ReceiverInfo.of("홍길동", "010-1234-5678");

            // When
            String normalized = receiverInfo.normalizedPhone();

            // Then
            assertEquals("01012345678", normalized);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ReceiverInfo는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            ReceiverInfo info1 = ReceiverInfo.of("홍길동", "01012345678");
            ReceiverInfo info2 = ReceiverInfo.of("홍길동", "01012345678");

            // When & Then
            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ReceiverInfo는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            ReceiverInfo info1 = ReceiverInfo.of("홍길동", "01012345678");
            ReceiverInfo info2 = ReceiverInfo.of("김철수", "01012345678");

            // When & Then
            assertNotEquals(info1, info2);
        }
    }
}

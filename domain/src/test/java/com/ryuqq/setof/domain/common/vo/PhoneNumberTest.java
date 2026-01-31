package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
@DisplayName("PhoneNumber Value Object 테스트")
class PhoneNumberTest {

    @Nested
    @DisplayName("of() - 전화번호 생성")
    class CreationTest {

        @Test
        @DisplayName("하이픈 포함 전화번호를 생성한다")
        void createWithHyphen() {
            // when
            var phone = PhoneNumber.of("02-1234-5678");

            // then
            assertThat(phone.value()).isEqualTo("02-1234-5678");
        }

        @Test
        @DisplayName("휴대폰 번호를 생성한다")
        void createMobileNumber() {
            // when
            var phone = PhoneNumber.of("010-1234-5678");

            // then
            assertThat(phone.value()).isEqualTo("010-1234-5678");
        }

        @Test
        @DisplayName("공백은 제거된다")
        void removeWhitespace() {
            // when
            var phone = PhoneNumber.of("  02-1234-5678  ");

            // then
            assertThat(phone.value()).isEqualTo("02-1234-5678");
        }

        @Test
        @DisplayName("하이픈 없이도 생성할 수 있다")
        void createWithoutHyphen() {
            // when
            var phone = PhoneNumber.of("0212345678");

            // then
            assertThat(phone.value()).isEqualTo("0212345678");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> PhoneNumber.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @ParameterizedTest
        @ValueSource(strings = {"abc", "02-abc-1234", "12345678901234567890123"})
        @DisplayName("유효하지 않은 형식이면 예외가 발생한다")
        void throwExceptionForInvalidFormat(String value) {
            assertThatThrownBy(() -> PhoneNumber.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유효하지 않은 전화번호");
        }
    }

    @Nested
    @DisplayName("digitsOnly() - 숫자만 반환")
    class DigitsOnlyTest {

        @Test
        @DisplayName("하이픈이 제거된 숫자만 반환한다")
        void returnsDigitsOnly() {
            // given
            var phone = PhoneNumber.of("02-1234-5678");

            // when
            String digits = phone.digitsOnly();

            // then
            assertThat(digits).isEqualTo("0212345678");
        }

        @Test
        @DisplayName("하이픈이 없는 번호는 그대로 반환한다")
        void returnsOriginalIfNoHyphen() {
            // given
            var phone = PhoneNumber.of("0212345678");

            // when
            String digits = phone.digitsOnly();

            // then
            assertThat(digits).isEqualTo("0212345678");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 전화번호는 동등하다")
        void equalPhoneNumbersAreEqual() {
            var phone1 = PhoneNumber.of("02-1234-5678");
            var phone2 = PhoneNumber.of("02-1234-5678");

            assertThat(phone1).isEqualTo(phone2);
            assertThat(phone1.hashCode()).isEqualTo(phone2.hashCode());
        }

        @Test
        @DisplayName("다른 전화번호는 동등하지 않다")
        void differentPhoneNumbersNotEqual() {
            var phone1 = PhoneNumber.of("02-1234-5678");
            var phone2 = PhoneNumber.of("02-1234-5679");

            assertThat(phone1).isNotEqualTo(phone2);
        }
    }
}

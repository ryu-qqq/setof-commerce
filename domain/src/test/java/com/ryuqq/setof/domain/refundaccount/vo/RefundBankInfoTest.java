package com.ryuqq.setof.domain.refundaccount.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundBankInfo Value Object 단위 테스트")
class RefundBankInfoTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 값으로 RefundBankInfo를 생성한다")
        void createWithValidValues() {
            // when
            RefundBankInfo bankInfo = RefundBankInfo.of("국민은행", "123456789012", "홍길동");

            // then
            assertThat(bankInfo.bankName()).isEqualTo("국민은행");
            assertThat(bankInfo.accountNumber()).isEqualTo("123456789012");
            assertThat(bankInfo.accountHolderName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("앞뒤 공백이 제거된다")
        void trimsLeadingAndTrailingWhitespace() {
            // when
            RefundBankInfo bankInfo = RefundBankInfo.of("  국민은행  ", "  123456789012  ", "  홍길동  ");

            // then
            assertThat(bankInfo.bankName()).isEqualTo("국민은행");
            assertThat(bankInfo.accountNumber()).isEqualTo("123456789012");
            assertThat(bankInfo.accountHolderName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("은행명이 null이면 예외가 발생한다")
        void nullBankNameThrowsException() {
            assertThatThrownBy(() -> RefundBankInfo.of(null, "123456789012", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("은행명은 필수입니다");
        }

        @Test
        @DisplayName("은행명이 빈 문자열이면 예외가 발생한다")
        void blankBankNameThrowsException() {
            assertThatThrownBy(() -> RefundBankInfo.of("   ", "123456789012", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("은행명은 필수입니다");
        }

        @Test
        @DisplayName("계좌번호가 null이면 예외가 발생한다")
        void nullAccountNumberThrowsException() {
            assertThatThrownBy(() -> RefundBankInfo.of("국민은행", null, "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("계좌번호는 필수입니다");
        }

        @Test
        @DisplayName("계좌번호가 빈 문자열이면 예외가 발생한다")
        void blankAccountNumberThrowsException() {
            assertThatThrownBy(() -> RefundBankInfo.of("국민은행", "   ", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("계좌번호는 필수입니다");
        }

        @Test
        @DisplayName("예금주명이 null이면 예외가 발생한다")
        void nullAccountHolderNameThrowsException() {
            assertThatThrownBy(() -> RefundBankInfo.of("국민은행", "123456789012", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("예금주는 필수입니다");
        }

        @Test
        @DisplayName("예금주명이 빈 문자열이면 예외가 발생한다")
        void blankAccountHolderNameThrowsException() {
            assertThatThrownBy(() -> RefundBankInfo.of("국민은행", "123456789012", "   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("예금주는 필수입니다");
        }
    }

    @Nested
    @DisplayName("maskedAccountNumber() - 계좌번호 마스킹")
    class MaskedAccountNumberTest {

        @Test
        @DisplayName("4자리보다 긴 계좌번호는 뒤 4자리만 표시된다")
        void accountNumberLongerThanFourDigitsMasked() {
            // given
            RefundBankInfo bankInfo = RefundBankInfo.of("국민은행", "123456789012", "홍길동");

            // when
            String masked = bankInfo.maskedAccountNumber();

            // then
            assertThat(masked).endsWith("9012");
            assertThat(masked).startsWith("*");
            assertThat(masked).hasSize(12);
        }

        @Test
        @DisplayName("4자리 이하의 계좌번호는 ****로 마스킹된다")
        void accountNumberFourOrFewerDigitsMaskedFully() {
            // given
            RefundBankInfo bankInfo = RefundBankInfo.of("국민은행", "1234", "홍길동");

            // when
            String masked = bankInfo.maskedAccountNumber();

            // then
            assertThat(masked).isEqualTo("****");
        }

        @Test
        @DisplayName("마스킹 결과의 마지막 4자리는 원래 계좌번호 마지막 4자리와 동일하다")
        void maskedNumberEndsWithLastFourDigits() {
            // given
            String accountNumber = "987654321098";
            RefundBankInfo bankInfo = RefundBankInfo.of("신한은행", accountNumber, "김철수");

            // when
            String masked = bankInfo.maskedAccountNumber();

            // then
            String lastFour = accountNumber.substring(accountNumber.length() - 4);
            assertThat(masked).endsWith(lastFour);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 RefundBankInfo는 동등하다")
        void sameValuesAreEqual() {
            // given
            RefundBankInfo info1 = RefundBankInfo.of("국민은행", "123456789012", "홍길동");
            RefundBankInfo info2 = RefundBankInfo.of("국민은행", "123456789012", "홍길동");

            // then
            assertThat(info1).isEqualTo(info2);
            assertThat(info1.hashCode()).isEqualTo(info2.hashCode());
        }

        @Test
        @DisplayName("은행명이 다른 RefundBankInfo는 동등하지 않다")
        void differentBankNameNotEquals() {
            // given
            RefundBankInfo info1 = RefundBankInfo.of("국민은행", "123456789012", "홍길동");
            RefundBankInfo info2 = RefundBankInfo.of("신한은행", "123456789012", "홍길동");

            // then
            assertThat(info1).isNotEqualTo(info2);
        }

        @Test
        @DisplayName("계좌번호가 다른 RefundBankInfo는 동등하지 않다")
        void differentAccountNumberNotEquals() {
            // given
            RefundBankInfo info1 = RefundBankInfo.of("국민은행", "123456789012", "홍길동");
            RefundBankInfo info2 = RefundBankInfo.of("국민은행", "987654321098", "홍길동");

            // then
            assertThat(info1).isNotEqualTo(info2);
        }

        @Test
        @DisplayName("예금주명이 다른 RefundBankInfo는 동등하지 않다")
        void differentHolderNameNotEquals() {
            // given
            RefundBankInfo info1 = RefundBankInfo.of("국민은행", "123456789012", "홍길동");
            RefundBankInfo info2 = RefundBankInfo.of("국민은행", "123456789012", "김철수");

            // then
            assertThat(info1).isNotEqualTo(info2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("RefundBankInfo는 record이므로 불변이다")
        void refundBankInfoIsImmutable() {
            // given
            RefundBankInfo bankInfo = RefundBankInfo.of("국민은행", "123456789012", "홍길동");

            // when - 새 인스턴스 생성
            RefundBankInfo newBankInfo = RefundBankInfo.of("신한은행", "987654321098", "김철수");

            // then - 원본은 변경되지 않는다
            assertThat(bankInfo.bankName()).isEqualTo("국민은행");
            assertThat(bankInfo.accountNumber()).isEqualTo("123456789012");
            assertThat(bankInfo.accountHolderName()).isEqualTo("홍길동");
        }
    }
}

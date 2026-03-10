package com.ryuqq.setof.domain.payment.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundAccountSnapshot Value Object 단위 테스트")
class RefundAccountSnapshotTest {

    @Nested
    @DisplayName("of() 팩토리 메서드 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 값으로 RefundAccountSnapshot을 생성한다")
        void createWithValidValues() {
            // when
            RefundAccountSnapshot snapshot =
                    RefundAccountSnapshot.of(1L, "004", "110-123-456789", "홍길동");

            // then
            assertThat(snapshot.userId()).isEqualTo(1L);
            assertThat(snapshot.bankCode()).isEqualTo("004");
            assertThat(snapshot.accountNumber()).isEqualTo("110-123-456789");
            assertThat(snapshot.accountHolderName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("userId가 0이면 예외가 발생한다")
        void createWithZeroUserId_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(0L, "004", "110-123-456789", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("userId must be positive");
        }

        @Test
        @DisplayName("userId가 음수이면 예외가 발생한다")
        void createWithNegativeUserId_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(-1L, "004", "110-123-456789", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("userId must be positive");
        }

        @Test
        @DisplayName("bankCode가 null이면 예외가 발생한다")
        void createWithNullBankCode_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(1L, null, "110-123-456789", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("은행 코드는 필수입니다");
        }

        @Test
        @DisplayName("bankCode가 빈 문자열이면 예외가 발생한다")
        void createWithBlankBankCode_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(1L, "  ", "110-123-456789", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("은행 코드는 필수입니다");
        }

        @Test
        @DisplayName("accountNumber가 null이면 예외가 발생한다")
        void createWithNullAccountNumber_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(1L, "004", null, "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("계좌번호는 필수입니다");
        }

        @Test
        @DisplayName("accountNumber가 빈 문자열이면 예외가 발생한다")
        void createWithBlankAccountNumber_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(1L, "004", "  ", "홍길동"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("계좌번호는 필수입니다");
        }

        @Test
        @DisplayName("accountHolderName이 null이면 예외가 발생한다")
        void createWithNullAccountHolderName_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(1L, "004", "110-123-456789", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("예금주는 필수입니다");
        }

        @Test
        @DisplayName("accountHolderName이 빈 문자열이면 예외가 발생한다")
        void createWithBlankAccountHolderName_ThrowsException() {
            assertThatThrownBy(() -> RefundAccountSnapshot.of(1L, "004", "110-123-456789", "  "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("예금주는 필수입니다");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동일하다")
        void sameValuesAreEqual() {
            // given
            RefundAccountSnapshot s1 = RefundAccountSnapshot.of(1L, "004", "110-123-456789", "홍길동");
            RefundAccountSnapshot s2 = RefundAccountSnapshot.of(1L, "004", "110-123-456789", "홍길동");

            // then
            assertThat(s1).isEqualTo(s2);
            assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
        }

        @Test
        @DisplayName("다른 계좌번호는 동일하지 않다")
        void differentAccountNumberNotEqual() {
            // given
            RefundAccountSnapshot s1 = RefundAccountSnapshot.of(1L, "004", "110-123-456789", "홍길동");
            RefundAccountSnapshot s2 = RefundAccountSnapshot.of(1L, "004", "999-999-999999", "홍길동");

            // then
            assertThat(s1).isNotEqualTo(s2);
        }
    }
}

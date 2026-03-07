package com.ryuqq.setof.domain.refundaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundAccountErrorCode 단위 테스트")
class RefundAccountErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("RefundAccountErrorCode는 ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND)
                    .isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("각 에러 코드 값 검증")
    class ErrorCodeValuesTest {

        @Test
        @DisplayName("REFUND_ACCOUNT_NOT_FOUND 에러 코드를 검증한다")
        void refundAccountNotFound() {
            assertThat(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND.getCode())
                    .isEqualTo("RFA-001");
            assertThat(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND.getHttpStatus())
                    .isEqualTo(404);
            assertThat(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND.getMessage())
                    .isEqualTo("환불 계좌를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ACCOUNT_VERIFICATION_FAILED 에러 코드를 검증한다")
        void accountVerificationFailed() {
            assertThat(RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED.getCode())
                    .isEqualTo("RFA-002");
            assertThat(RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED.getHttpStatus())
                    .isEqualTo(400);
            assertThat(RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED.getMessage())
                    .isEqualTo("계좌 실명 검증에 실패했습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(RefundAccountErrorCode.values())
                    .containsExactly(
                            RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND,
                            RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED);
        }
    }
}

package com.ryuqq.setof.domain.refundaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * RefundAccountErrorCode 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundAccountErrorCode 단위 테스트")
class RefundAccountErrorCodeTest {

    @Test
    @DisplayName("성공 - 에러 코드가 올바른 값을 반환한다")
    void shouldReturnCorrectCode() {
        // When & Then
        assertThat(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND.getCode()).isEqualTo("RFA-001");
    }

    @Test
    @DisplayName("성공 - HTTP 상태 코드가 올바른 값을 반환한다")
    void shouldReturnCorrectHttpStatus() {
        // When & Then
        assertThat(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND.getHttpStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("성공 - 메시지가 올바른 값을 반환한다")
    void shouldReturnCorrectMessage() {
        // When & Then
        assertThat(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND.getMessage())
                .isEqualTo("환불계좌를 찾을 수 없습니다.");
    }
}

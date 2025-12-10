package com.ryuqq.setof.domain.refundaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * RefundAccountAlreadyExistsException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundAccountAlreadyExistsException 단위 테스트")
class RefundAccountAlreadyExistsExceptionTest {

    @Test
    @DisplayName("성공 - memberId로 예외를 생성한다")
    void shouldCreateWithMemberId() {
        // Given
        UUID memberId = UUID.randomUUID();

        // When
        RefundAccountAlreadyExistsException exception =
                new RefundAccountAlreadyExistsException(memberId);

        // Then
        assertThat(exception.getMessage()).contains("이미 등록된 환불계좌가 있습니다");
        assertThat(exception.getMessage()).contains(memberId.toString());
        assertThat(exception.getErrorCode()).isEqualTo(RefundAccountErrorCode.REFUND_ACCOUNT_ALREADY_EXISTS);
    }
}

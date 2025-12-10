package com.ryuqq.setof.domain.refundaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * RefundAccountNotOwnerException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundAccountNotOwnerException 단위 테스트")
class RefundAccountNotOwnerExceptionTest {

    @Test
    @DisplayName("성공 - refundAccountId와 requestMemberId로 예외를 생성한다")
    void shouldCreateWithRefundAccountIdAndRequestMemberId() {
        // Given
        Long refundAccountId = 1L;
        UUID requestMemberId = UUID.randomUUID();

        // When
        RefundAccountNotOwnerException exception =
                new RefundAccountNotOwnerException(refundAccountId, requestMemberId);

        // Then
        assertThat(exception.getMessage()).contains("해당 환불계좌에 대한 권한이 없습니다");
        assertThat(exception.getMessage()).contains("1");
        assertThat(exception.getMessage()).contains(requestMemberId.toString());
        assertThat(exception.getErrorCode()).isEqualTo(RefundAccountErrorCode.NOT_OWNER);
    }
}

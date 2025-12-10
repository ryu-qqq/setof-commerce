package com.ryuqq.setof.domain.refundaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * RefundAccountNotFoundException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundAccountNotFoundException 단위 테스트")
class RefundAccountNotFoundExceptionTest {

    @Nested
    @DisplayName("refundAccountId 생성자")
    class RefundAccountIdConstructor {

        @Test
        @DisplayName("성공 - refundAccountId로 예외를 생성한다")
        void shouldCreateWithRefundAccountId() {
            // Given
            Long refundAccountId = 1L;

            // When
            RefundAccountNotFoundException exception =
                    new RefundAccountNotFoundException(refundAccountId);

            // Then
            assertThat(exception.getMessage()).contains("환불계좌를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode()).isEqualTo(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("memberId 생성자")
    class MemberIdConstructor {

        @Test
        @DisplayName("성공 - memberId로 예외를 생성한다")
        void shouldCreateWithMemberId() {
            // Given
            UUID memberId = UUID.randomUUID();

            // When
            RefundAccountNotFoundException exception =
                    new RefundAccountNotFoundException(memberId);

            // Then
            assertThat(exception.getMessage()).contains("환불계좌를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains(memberId.toString());
        }
    }
}

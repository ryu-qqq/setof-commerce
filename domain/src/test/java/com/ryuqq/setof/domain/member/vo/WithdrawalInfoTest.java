package com.ryuqq.setof.domain.member.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.member.exception.InvalidWithdrawalInfoException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("WithdrawalInfo Value Object")
class WithdrawalInfoTest {

    @Nested
    @DisplayName("생성 테스트")
    class Creation {

        @Test
        @DisplayName("유효한 값으로 WithdrawalInfo 생성 성공")
        void shouldCreateWithdrawalInfoWithValidData() {
            Instant withdrawnAt = Instant.parse("2025-01-15T01:30:00Z");

            WithdrawalInfo info = WithdrawalInfo.of(WithdrawalReason.RARELY_USED, withdrawnAt);

            assertNotNull(info);
            assertEquals(WithdrawalReason.RARELY_USED, info.reason());
            assertEquals(withdrawnAt, info.withdrawnAt());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailure {

        @Test
        @DisplayName("탈퇴 사유가 null이면 예외 발생")
        void shouldThrowExceptionWhenReasonIsNull() {
            Instant withdrawnAt = Instant.now();

            InvalidWithdrawalInfoException exception =
                    assertThrows(
                            InvalidWithdrawalInfoException.class,
                            () -> WithdrawalInfo.of(null, withdrawnAt));

            assertTrue(exception.getMessage().contains("탈퇴 사유"));
        }

        @Test
        @DisplayName("탈퇴 일시가 null이면 예외 발생")
        void shouldThrowExceptionWhenWithdrawnAtIsNull() {
            InvalidWithdrawalInfoException exception =
                    assertThrows(
                            InvalidWithdrawalInfoException.class,
                            () -> WithdrawalInfo.of(WithdrawalReason.OTHER, null));

            assertTrue(exception.getMessage().contains("탈퇴 일시"));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class Equality {

        @Test
        @DisplayName("같은 값을 가진 WithdrawalInfo는 동등")
        void shouldBeEqualWhenSameValues() {
            Instant withdrawnAt = Instant.parse("2025-01-15T01:30:00Z");

            WithdrawalInfo info1 = WithdrawalInfo.of(WithdrawalReason.RARELY_USED, withdrawnAt);
            WithdrawalInfo info2 = WithdrawalInfo.of(WithdrawalReason.RARELY_USED, withdrawnAt);

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }
    }
}

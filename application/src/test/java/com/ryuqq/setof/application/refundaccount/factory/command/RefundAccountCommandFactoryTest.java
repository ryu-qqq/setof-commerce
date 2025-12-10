package com.ryuqq.setof.application.refundaccount.factory.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RefundAccountCommandFactory")
class RefundAccountCommandFactoryTest {

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private RefundAccountCommandFactory factory;
    private ClockHolder clockHolder;

    @BeforeEach
    void setUp() {
        clockHolder = () -> FIXED_CLOCK;
        factory = new RefundAccountCommandFactory(clockHolder);
    }

    @Nested
    @DisplayName("createVerified")
    class CreateVerifiedTest {

        @Test
        @DisplayName("RegisterRefundAccountCommand로 검증 완료 RefundAccount 생성 성공")
        void shouldCreateVerifiedRefundAccountFromCommand() {
            // Given
            UUID memberId = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
            RegisterRefundAccountCommand command =
                    new RegisterRefundAccountCommand(memberId, 1L, "1234567890123", "홍길동");

            // When
            RefundAccount result = factory.createVerified(command);

            // Then
            assertNotNull(result);
            assertNull(result.getIdValue()); // 신규 생성이므로 ID 없음
            assertEquals(memberId, result.getMemberId());
            assertEquals(1L, result.getBankId());
            assertEquals("1234567890123", result.getAccountNumberValue());
            assertEquals("홍길동", result.getAccountHolderNameValue());
            assertTrue(result.isVerified()); // 검증 완료 상태
            assertNotNull(result.getVerifiedAt());
        }

        @Test
        @DisplayName("다른 회원 및 은행으로 생성 성공")
        void shouldCreateRefundAccountWithDifferentMemberAndBank() {
            // Given
            UUID memberId = UUID.fromString("11111111-1111-1111-1111-111111111111");
            RegisterRefundAccountCommand command =
                    new RegisterRefundAccountCommand(memberId, 5L, "9876543210", "김철수");

            // When
            RefundAccount result = factory.createVerified(command);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.getMemberId());
            assertEquals(5L, result.getBankId());
            assertEquals("9876543210", result.getAccountNumberValue());
            assertEquals("김철수", result.getAccountHolderNameValue());
        }
    }

    @Nested
    @DisplayName("applyUpdateVerified")
    class ApplyUpdateVerifiedTest {

        @Test
        @DisplayName("UpdateRefundAccountCommand로 RefundAccount 수정 성공")
        void shouldApplyUpdateToRefundAccount() {
            // Given
            RefundAccount refundAccount = RefundAccountFixture.createUnverifiedWithId(1L);
            UUID memberId = refundAccount.getMemberId();
            UpdateRefundAccountCommand command =
                    new UpdateRefundAccountCommand(memberId, 1L, 2L, "5555666677778888", "박영희");

            // When
            factory.applyUpdateVerified(refundAccount, command);

            // Then
            assertEquals(2L, refundAccount.getBankId());
            assertEquals("5555666677778888", refundAccount.getAccountNumberValue());
            assertEquals("박영희", refundAccount.getAccountHolderNameValue());
            assertTrue(refundAccount.isVerified()); // 수정 후 검증 완료 상태
        }

        @Test
        @DisplayName("기존 검증 완료 상태에서도 수정 성공")
        void shouldUpdateAlreadyVerifiedRefundAccount() {
            // Given
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            assertTrue(refundAccount.isVerified()); // 이미 검증 완료
            UUID memberId = refundAccount.getMemberId();

            UpdateRefundAccountCommand command =
                    new UpdateRefundAccountCommand(memberId, 1L, 3L, "1111222233334444", "이철수");

            // When
            factory.applyUpdateVerified(refundAccount, command);

            // Then
            assertEquals(3L, refundAccount.getBankId());
            assertEquals("1111222233334444", refundAccount.getAccountNumberValue());
            assertEquals("이철수", refundAccount.getAccountHolderNameValue());
            assertTrue(refundAccount.isVerified());
        }
    }
}

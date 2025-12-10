package com.ryuqq.setof.application.refundaccount.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.manager.command.RefundAccountPersistenceManager;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotOwnerException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("DeleteRefundAccountService")
@ExtendWith(MockitoExtension.class)
class DeleteRefundAccountServiceTest {

    @Mock private RefundAccountReadManager refundAccountReadManager;
    @Mock private RefundAccountPersistenceManager refundAccountPersistenceManager;
    @Mock private ClockHolder clockHolder;

    private DeleteRefundAccountService deleteRefundAccountService;

    @BeforeEach
    void setUp() {
        deleteRefundAccountService =
                new DeleteRefundAccountService(
                        refundAccountReadManager, refundAccountPersistenceManager, clockHolder);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("환불계좌 삭제 성공")
        void shouldDeleteRefundAccount() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long refundAccountId = 1L;
            DeleteRefundAccountCommand command =
                    DeleteRefundAccountCommand.of(memberId, refundAccountId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);

            when(refundAccountReadManager.findById(refundAccountId)).thenReturn(refundAccount);
            when(clockHolder.getClock()).thenReturn(RefundAccountFixture.FIXED_CLOCK);

            // When
            deleteRefundAccountService.execute(command);

            // Then
            verify(refundAccountPersistenceManager, times(1)).persist(refundAccount);
        }

        @Test
        @DisplayName("소유권 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenOwnershipValidationFails() {
            // Given
            UUID otherMemberId = UUID.randomUUID();
            Long refundAccountId = 1L;
            DeleteRefundAccountCommand command =
                    DeleteRefundAccountCommand.of(otherMemberId, refundAccountId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);

            when(refundAccountReadManager.findById(refundAccountId)).thenReturn(refundAccount);

            // When & Then
            assertThrows(
                    RefundAccountNotOwnerException.class,
                    () -> deleteRefundAccountService.execute(command));
            verify(refundAccountPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("삭제 후 영속화 호출 확인")
        void shouldCallPersistAfterDelete() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long refundAccountId = 1L;
            DeleteRefundAccountCommand command =
                    DeleteRefundAccountCommand.of(memberId, refundAccountId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);

            when(refundAccountReadManager.findById(refundAccountId)).thenReturn(refundAccount);
            when(clockHolder.getClock()).thenReturn(RefundAccountFixture.FIXED_CLOCK);

            // When
            deleteRefundAccountService.execute(command);

            // Then
            verify(refundAccountReadManager, times(1)).findById(refundAccountId);
            verify(clockHolder, times(1)).getClock();
            verify(refundAccountPersistenceManager, times(1)).persist(refundAccount);
        }
    }
}

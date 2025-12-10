package com.ryuqq.setof.application.refundaccount.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.refundaccount.port.out.query.RefundAccountQueryPort;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RefundAccountReadManager")
@ExtendWith(MockitoExtension.class)
class RefundAccountReadManagerTest {

    @Mock private RefundAccountQueryPort refundAccountQueryPort;

    private RefundAccountReadManager refundAccountReadManager;

    @BeforeEach
    void setUp() {
        refundAccountReadManager = new RefundAccountReadManager(refundAccountQueryPort);
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 환불계좌 조회 성공")
        void shouldReturnRefundAccount() {
            // Given
            Long refundAccountId = 1L;
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);

            when(refundAccountQueryPort.findById(any())).thenReturn(Optional.of(refundAccount));

            // When
            RefundAccount result = refundAccountReadManager.findById(refundAccountId);

            // Then
            assertNotNull(result);
            assertEquals(refundAccountId, result.getIdValue());
        }

        @Test
        @DisplayName("존재하지 않는 ID 조회 시 예외 발생")
        void shouldThrowExceptionWhenNotFound() {
            // Given
            Long refundAccountId = 999L;

            when(refundAccountQueryPort.findById(any())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    RefundAccountNotFoundException.class,
                    () -> refundAccountReadManager.findById(refundAccountId));
        }
    }

    @Nested
    @DisplayName("findByMemberId")
    class FindByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 환불계좌 조회 성공")
        void shouldReturnRefundAccountByMemberId() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);

            when(refundAccountQueryPort.findByMemberId(memberId))
                    .thenReturn(Optional.of(refundAccount));

            // When
            Optional<RefundAccount> result = refundAccountReadManager.findByMemberId(memberId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(memberId, result.get().getMemberId());
        }

        @Test
        @DisplayName("환불계좌가 없을 때 빈 Optional 반환")
        void shouldReturnEmptyWhenNoAccount() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;

            when(refundAccountQueryPort.findByMemberId(memberId)).thenReturn(Optional.empty());

            // When
            Optional<RefundAccount> result = refundAccountReadManager.findByMemberId(memberId);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("existsByMemberId")
    class ExistsByMemberIdTest {

        @Test
        @DisplayName("환불계좌 존재 시 true 반환")
        void shouldReturnTrueWhenExists() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;

            when(refundAccountQueryPort.existsByMemberId(memberId)).thenReturn(true);

            // When
            boolean result = refundAccountReadManager.existsByMemberId(memberId);

            // Then
            assertTrue(result);
            verify(refundAccountQueryPort, times(1)).existsByMemberId(memberId);
        }

        @Test
        @DisplayName("환불계좌 없을 시 false 반환")
        void shouldReturnFalseWhenNotExists() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;

            when(refundAccountQueryPort.existsByMemberId(memberId)).thenReturn(false);

            // When
            boolean result = refundAccountReadManager.existsByMemberId(memberId);

            // Then
            assertFalse(result);
        }
    }
}

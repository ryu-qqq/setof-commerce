package com.ryuqq.setof.application.refundaccount.manager.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.refundaccount.port.out.command.RefundAccountPersistencePort;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RefundAccountPersistenceManager")
@ExtendWith(MockitoExtension.class)
class RefundAccountPersistenceManagerTest {

    @Mock private RefundAccountPersistencePort refundAccountPersistencePort;

    private RefundAccountPersistenceManager refundAccountPersistenceManager;

    @BeforeEach
    void setUp() {
        refundAccountPersistenceManager =
                new RefundAccountPersistenceManager(refundAccountPersistencePort);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("환불계좌 저장 성공")
        void shouldPersistRefundAccount() {
            // Given
            RefundAccount refundAccount = RefundAccountFixture.createNewVerified();
            RefundAccountId expectedId = RefundAccountId.of(1L);

            when(refundAccountPersistencePort.persist(refundAccount)).thenReturn(expectedId);

            // When
            RefundAccountId result = refundAccountPersistenceManager.persist(refundAccount);

            // Then
            assertNotNull(result);
            assertEquals(expectedId.value(), result.value());
            verify(refundAccountPersistencePort, times(1)).persist(refundAccount);
        }

        @Test
        @DisplayName("기존 환불계좌 수정 후 저장")
        void shouldPersistExistingRefundAccount() {
            // Given
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            RefundAccountId expectedId = RefundAccountId.of(1L);

            when(refundAccountPersistencePort.persist(refundAccount)).thenReturn(expectedId);

            // When
            RefundAccountId result = refundAccountPersistenceManager.persist(refundAccount);

            // Then
            assertEquals(1L, result.value());
            verify(refundAccountPersistencePort, times(1)).persist(refundAccount);
        }
    }
}

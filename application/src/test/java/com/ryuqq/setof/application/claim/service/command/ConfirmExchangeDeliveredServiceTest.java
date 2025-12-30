package com.ryuqq.setof.application.claim.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.claim.dto.command.ConfirmExchangeDeliveredCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.ClaimFixture;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.exception.ClaimNotFoundException;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ConfirmExchangeDeliveredService")
@ExtendWith(MockitoExtension.class)
class ConfirmExchangeDeliveredServiceTest {

    @Mock private ClaimReadManager claimReadManager;
    @Mock private ClaimPersistencePort claimPersistencePort;
    @Mock private ClockHolder clockHolder;

    private ConfirmExchangeDeliveredService confirmExchangeDeliveredService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.of("UTC"));
        lenient().when(clockHolder.getClock()).thenReturn(fixedClock);
        confirmExchangeDeliveredService =
                new ConfirmExchangeDeliveredService(
                        claimReadManager, claimPersistencePort, clockHolder);
    }

    @Nested
    @DisplayName("confirmDelivered")
    class ConfirmDeliveredTest {

        @Test
        @DisplayName("교환품 배송 완료 확인 성공")
        void shouldConfirmExchangeDeliveredSuccessfully() {
            // Given
            Claim claim = ClaimFixture.createExchangeShippedClaim();
            String claimId = claim.claimId().value();

            ConfirmExchangeDeliveredCommand command = new ConfirmExchangeDeliveredCommand(claimId);

            when(claimReadManager.findById(claimId)).thenReturn(claim);

            // When
            confirmExchangeDeliveredService.confirmDelivered(command);

            // Then
            verify(claimReadManager, times(1)).findById(claimId);
            verify(claimPersistencePort, times(1)).persist(any(Claim.class));
        }

        @Test
        @DisplayName("존재하지 않는 클레임이면 예외 발생")
        void shouldThrowExceptionWhenClaimNotFound() {
            // Given
            String claimId = "CLM-NOT-EXIST";

            ConfirmExchangeDeliveredCommand command = new ConfirmExchangeDeliveredCommand(claimId);

            when(claimReadManager.findById(claimId))
                    .thenThrow(ClaimNotFoundException.byId(claimId));

            // When & Then
            assertThrows(
                    ClaimNotFoundException.class,
                    () -> confirmExchangeDeliveredService.confirmDelivered(command));
            verify(claimPersistencePort, never()).persist(any());
        }
    }
}

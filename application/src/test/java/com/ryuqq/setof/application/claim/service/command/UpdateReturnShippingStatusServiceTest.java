package com.ryuqq.setof.application.claim.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.claim.dto.command.UpdateReturnShippingStatusCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.ClaimFixture;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.exception.ClaimNotFoundException;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingStatus;
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

@DisplayName("UpdateReturnShippingStatusService")
@ExtendWith(MockitoExtension.class)
class UpdateReturnShippingStatusServiceTest {

    @Mock private ClaimReadManager claimReadManager;
    @Mock private ClaimPersistencePort claimPersistencePort;
    @Mock private ClockHolder clockHolder;

    private UpdateReturnShippingStatusService updateReturnShippingStatusService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.of("UTC"));
        lenient().when(clockHolder.getClock()).thenReturn(fixedClock);
        updateReturnShippingStatusService =
                new UpdateReturnShippingStatusService(
                        claimReadManager, claimPersistencePort, clockHolder);
    }

    @Nested
    @DisplayName("updateStatus")
    class UpdateStatusTest {

        @Test
        @DisplayName("배송 상태 업데이트 성공")
        void shouldUpdateStatusSuccessfully() {
            // Given
            Claim claim = ClaimFixture.createPickupScheduledClaim();
            String claimId = claim.claimId().value();
            ReturnShippingStatus newStatus = ReturnShippingStatus.PICKED_UP;
            String trackingNumber = "9876543210";

            UpdateReturnShippingStatusCommand command =
                    new UpdateReturnShippingStatusCommand(claimId, newStatus, trackingNumber);

            when(claimReadManager.findById(claimId)).thenReturn(claim);

            // When
            updateReturnShippingStatusService.updateStatus(command);

            // Then
            verify(claimReadManager, times(1)).findById(claimId);
            verify(claimPersistencePort, times(1)).persist(any(Claim.class));
        }

        @Test
        @DisplayName("존재하지 않는 클레임이면 예외 발생")
        void shouldThrowExceptionWhenClaimNotFound() {
            // Given
            String claimId = "CLM-NOT-EXIST";
            ReturnShippingStatus newStatus = ReturnShippingStatus.PICKED_UP;

            UpdateReturnShippingStatusCommand command =
                    new UpdateReturnShippingStatusCommand(claimId, newStatus, null);

            when(claimReadManager.findById(claimId))
                    .thenThrow(ClaimNotFoundException.byId(claimId));

            // When & Then
            assertThrows(
                    ClaimNotFoundException.class,
                    () -> updateReturnShippingStatusService.updateStatus(command));
            verify(claimPersistencePort, never()).persist(any());
        }
    }
}

package com.ryuqq.setof.application.claim.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.claim.dto.command.ScheduleReturnPickupCommand;
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

@DisplayName("ScheduleReturnPickupService")
@ExtendWith(MockitoExtension.class)
class ScheduleReturnPickupServiceTest {

    @Mock private ClaimReadManager claimReadManager;
    @Mock private ClaimPersistencePort claimPersistencePort;
    @Mock private ClockHolder clockHolder;

    private ScheduleReturnPickupService scheduleReturnPickupService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.of("UTC"));
        lenient().when(clockHolder.getClock()).thenReturn(fixedClock);
        scheduleReturnPickupService =
                new ScheduleReturnPickupService(
                        claimReadManager, claimPersistencePort, clockHolder);
    }

    @Nested
    @DisplayName("schedulePickup")
    class SchedulePickupTest {

        @Test
        @DisplayName("수거 예약 성공")
        void shouldSchedulePickupSuccessfully() {
            // Given
            Claim claim = ClaimFixture.createApprovedReturnClaim();
            String claimId = claim.claimId().value();
            Instant scheduledAt = Instant.now().plusSeconds(86400);
            String pickupAddress = "서울시 강남구 테헤란로 123";
            String customerPhone = "010-1234-5678";

            ScheduleReturnPickupCommand command =
                    new ScheduleReturnPickupCommand(
                            claimId, scheduledAt, pickupAddress, customerPhone);

            when(claimReadManager.findById(claimId)).thenReturn(claim);

            // When
            scheduleReturnPickupService.schedulePickup(command);

            // Then
            verify(claimReadManager, times(1)).findById(claimId);
            verify(claimPersistencePort, times(1)).persist(any(Claim.class));
        }

        @Test
        @DisplayName("존재하지 않는 클레임이면 예외 발생")
        void shouldThrowExceptionWhenClaimNotFound() {
            // Given
            String claimId = "CLM-NOT-EXIST";
            Instant scheduledAt = Instant.now().plusSeconds(86400);
            String pickupAddress = "서울시 강남구 테헤란로 123";
            String customerPhone = "010-1234-5678";

            ScheduleReturnPickupCommand command =
                    new ScheduleReturnPickupCommand(
                            claimId, scheduledAt, pickupAddress, customerPhone);

            when(claimReadManager.findById(claimId))
                    .thenThrow(ClaimNotFoundException.byId(claimId));

            // When & Then
            assertThrows(
                    ClaimNotFoundException.class,
                    () -> scheduleReturnPickupService.schedulePickup(command));
            verify(claimPersistencePort, never()).persist(any());
        }
    }
}

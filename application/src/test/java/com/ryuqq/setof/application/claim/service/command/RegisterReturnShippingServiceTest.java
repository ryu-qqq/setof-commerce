package com.ryuqq.setof.application.claim.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.claim.dto.command.RegisterReturnShippingCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.ClaimFixture;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.exception.ClaimNotFoundException;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingMethod;
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

@DisplayName("RegisterReturnShippingService")
@ExtendWith(MockitoExtension.class)
class RegisterReturnShippingServiceTest {

    @Mock private ClaimReadManager claimReadManager;
    @Mock private ClaimPersistencePort claimPersistencePort;
    @Mock private ClockHolder clockHolder;

    private RegisterReturnShippingService registerReturnShippingService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.of("UTC"));
        lenient().when(clockHolder.getClock()).thenReturn(fixedClock);
        registerReturnShippingService =
                new RegisterReturnShippingService(
                        claimReadManager, claimPersistencePort, clockHolder);
    }

    @Nested
    @DisplayName("registerShipping")
    class RegisterShippingTest {

        @Test
        @DisplayName("반품 송장 등록 성공")
        void shouldRegisterShippingSuccessfully() {
            // Given
            Claim claim = ClaimFixture.createApprovedReturnClaim();
            String claimId = claim.claimId().value();
            String trackingNumber = "1234567890";
            String carrier = "CJ대한통운";

            RegisterReturnShippingCommand command =
                    new RegisterReturnShippingCommand(
                            claimId, ReturnShippingMethod.CUSTOMER_SHIP, trackingNumber, carrier);

            when(claimReadManager.findById(claimId)).thenReturn(claim);

            // When
            registerReturnShippingService.registerShipping(command);

            // Then
            verify(claimReadManager, times(1)).findById(claimId);
            verify(claimPersistencePort, times(1)).persist(any(Claim.class));
        }

        @Test
        @DisplayName("존재하지 않는 클레임이면 예외 발생")
        void shouldThrowExceptionWhenClaimNotFound() {
            // Given
            String claimId = "CLM-NOT-EXIST";
            String trackingNumber = "1234567890";
            String carrier = "CJ대한통운";

            RegisterReturnShippingCommand command =
                    new RegisterReturnShippingCommand(
                            claimId, ReturnShippingMethod.CUSTOMER_SHIP, trackingNumber, carrier);

            when(claimReadManager.findById(claimId))
                    .thenThrow(ClaimNotFoundException.byId(claimId));

            // When & Then
            assertThrows(
                    ClaimNotFoundException.class,
                    () -> registerReturnShippingService.registerShipping(command));
            verify(claimPersistencePort, never()).persist(any());
        }
    }
}

package com.ryuqq.setof.application.claim.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.claim.dto.command.RegisterExchangeShippingCommand;
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

@DisplayName("RegisterExchangeShippingService")
@ExtendWith(MockitoExtension.class)
class RegisterExchangeShippingServiceTest {

    @Mock private ClaimReadManager claimReadManager;
    @Mock private ClaimPersistencePort claimPersistencePort;
    @Mock private ClockHolder clockHolder;

    private RegisterExchangeShippingService registerExchangeShippingService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.of("UTC"));
        lenient().when(clockHolder.getClock()).thenReturn(fixedClock);
        registerExchangeShippingService =
                new RegisterExchangeShippingService(
                        claimReadManager, claimPersistencePort, clockHolder);
    }

    @Nested
    @DisplayName("registerShipping")
    class RegisterShippingTest {

        @Test
        @DisplayName("교환품 발송 등록 성공")
        void shouldRegisterExchangeShippingSuccessfully() {
            // Given
            Claim claim = ClaimFixture.createReturnReceivedClaim();
            // Note: createReturnReceivedClaim is RETURN type, we need EXCHANGE type with RECEIVED
            // status
            // Using createExchangeShippedClaim but before exchange shipping (need custom fixture)
            Claim exchangeClaim = ClaimFixture.createCustomExchange();
            String claimId = exchangeClaim.claimId().value();
            String trackingNumber = "5555555555";
            String carrier = "한진택배";

            RegisterExchangeShippingCommand command =
                    new RegisterExchangeShippingCommand(claimId, trackingNumber, carrier);

            when(claimReadManager.findById(claimId)).thenReturn(exchangeClaim);

            // When
            registerExchangeShippingService.registerShipping(command);

            // Then
            verify(claimReadManager, times(1)).findById(claimId);
            verify(claimPersistencePort, times(1)).persist(any(Claim.class));
        }

        @Test
        @DisplayName("존재하지 않는 클레임이면 예외 발생")
        void shouldThrowExceptionWhenClaimNotFound() {
            // Given
            String claimId = "CLM-NOT-EXIST";
            String trackingNumber = "5555555555";
            String carrier = "한진택배";

            RegisterExchangeShippingCommand command =
                    new RegisterExchangeShippingCommand(claimId, trackingNumber, carrier);

            when(claimReadManager.findById(claimId))
                    .thenThrow(ClaimNotFoundException.byId(claimId));

            // When & Then
            assertThrows(
                    ClaimNotFoundException.class,
                    () -> registerExchangeShippingService.registerShipping(command));
            verify(claimPersistencePort, never()).persist(any());
        }
    }
}

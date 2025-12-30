package com.ryuqq.setof.application.claim.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.claim.dto.command.ConfirmReturnReceivedCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.ClaimFixture;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.exception.ClaimNotFoundException;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.claim.vo.ClaimType;
import com.ryuqq.setof.domain.claim.vo.InspectionResult;
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

@DisplayName("ConfirmReturnReceivedService")
@ExtendWith(MockitoExtension.class)
class ConfirmReturnReceivedServiceTest {

    @Mock private ClaimReadManager claimReadManager;
    @Mock private ClaimPersistencePort claimPersistencePort;
    @Mock private ClockHolder clockHolder;

    private ConfirmReturnReceivedService confirmReturnReceivedService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.of("UTC"));
        lenient().when(clockHolder.getClock()).thenReturn(fixedClock);
        confirmReturnReceivedService =
                new ConfirmReturnReceivedService(
                        claimReadManager, claimPersistencePort, clockHolder);
    }

    @Nested
    @DisplayName("confirmReceived")
    class ConfirmReceivedTest {

        @Test
        @DisplayName("반품 수령 확인 성공 (검수 합격)")
        void shouldConfirmReceivedWithPassResult() {
            // Given
            Claim claim =
                    ClaimFixture.createCustom(
                            10L,
                            "CLM-TEST-0010",
                            "ORD-TEST-0010",
                            ClaimType.RETURN,
                            ClaimStatus.IN_PROGRESS,
                            ReturnShippingStatus.IN_TRANSIT);
            String claimId = claim.claimId().value();
            InspectionResult inspectionResult = InspectionResult.PASS;
            String inspectionNote = "검수 완료 - 이상 없음";

            ConfirmReturnReceivedCommand command =
                    new ConfirmReturnReceivedCommand(claimId, inspectionResult, inspectionNote);

            when(claimReadManager.findById(claimId)).thenReturn(claim);

            // When
            confirmReturnReceivedService.confirmReceived(command);

            // Then
            verify(claimReadManager, times(1)).findById(claimId);
            verify(claimPersistencePort, times(1)).persist(any(Claim.class));
        }

        @Test
        @DisplayName("반품 수령 확인 성공 (검수 불합격)")
        void shouldConfirmReceivedWithFailResult() {
            // Given
            Claim claim =
                    ClaimFixture.createCustom(
                            11L,
                            "CLM-TEST-0011",
                            "ORD-TEST-0011",
                            ClaimType.RETURN,
                            ClaimStatus.IN_PROGRESS,
                            ReturnShippingStatus.IN_TRANSIT);
            String claimId = claim.claimId().value();
            InspectionResult inspectionResult = InspectionResult.FAIL;
            String inspectionNote = "상품 훼손 심함";

            ConfirmReturnReceivedCommand command =
                    new ConfirmReturnReceivedCommand(claimId, inspectionResult, inspectionNote);

            when(claimReadManager.findById(claimId)).thenReturn(claim);

            // When
            confirmReturnReceivedService.confirmReceived(command);

            // Then
            verify(claimReadManager, times(1)).findById(claimId);
            verify(claimPersistencePort, times(1)).persist(any(Claim.class));
        }

        @Test
        @DisplayName("존재하지 않는 클레임이면 예외 발생")
        void shouldThrowExceptionWhenClaimNotFound() {
            // Given
            String claimId = "CLM-NOT-EXIST";
            InspectionResult inspectionResult = InspectionResult.PASS;

            ConfirmReturnReceivedCommand command =
                    new ConfirmReturnReceivedCommand(claimId, inspectionResult, null);

            when(claimReadManager.findById(claimId))
                    .thenThrow(ClaimNotFoundException.byId(claimId));

            // When & Then
            assertThrows(
                    ClaimNotFoundException.class,
                    () -> confirmReturnReceivedService.confirmReceived(command));
            verify(claimPersistencePort, never()).persist(any());
        }
    }
}

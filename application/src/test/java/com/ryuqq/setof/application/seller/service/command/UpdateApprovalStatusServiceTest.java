package com.ryuqq.setof.application.seller.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.manager.command.SellerPersistenceManager;
import com.ryuqq.setof.application.seller.manager.query.SellerReadManager;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.ApprovalStatus;
import com.ryuqq.setof.domain.seller.vo.BusinessAddress;
import com.ryuqq.setof.domain.seller.vo.BusinessInfo;
import com.ryuqq.setof.domain.seller.vo.CsEmail;
import com.ryuqq.setof.domain.seller.vo.CsLandlinePhone;
import com.ryuqq.setof.domain.seller.vo.CsMobilePhone;
import com.ryuqq.setof.domain.seller.vo.CustomerServiceInfo;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.seller.vo.SellerName;
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

/**
 * UpdateApprovalStatusService 테스트
 *
 * <p>셀러 승인 상태 변경 서비스에 대한 단위 테스트
 */
@DisplayName("UpdateApprovalStatusService")
@ExtendWith(MockitoExtension.class)
class UpdateApprovalStatusServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME, ZoneId.of("UTC"));

    @Mock private SellerReadManager sellerReadManager;
    @Mock private SellerPersistenceManager sellerPersistenceManager;
    @Mock private ClockHolder clockHolder;

    private UpdateApprovalStatusService updateApprovalStatusService;

    @BeforeEach
    void setUp() {
        updateApprovalStatusService =
                new UpdateApprovalStatusService(
                        sellerReadManager, sellerPersistenceManager, clockHolder);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("PENDING 상태에서 APPROVED로 변경 성공")
        void shouldApproveFromPending() {
            // Given
            Long sellerId = 1L;
            UpdateApprovalStatusCommand command =
                    UpdateApprovalStatusCommand.of(sellerId, "APPROVED");
            Seller pendingSeller = createPendingSeller();

            when(sellerReadManager.findById(sellerId)).thenReturn(pendingSeller);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateApprovalStatusService.execute(command);

            // Then
            verify(sellerReadManager).findById(sellerId);
            verify(sellerPersistenceManager).persist(any(Seller.class));
        }

        @Test
        @DisplayName("PENDING 상태에서 REJECTED로 변경 성공")
        void shouldRejectFromPending() {
            // Given
            Long sellerId = 1L;
            UpdateApprovalStatusCommand command =
                    UpdateApprovalStatusCommand.of(sellerId, "REJECTED");
            Seller pendingSeller = createPendingSeller();

            when(sellerReadManager.findById(sellerId)).thenReturn(pendingSeller);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateApprovalStatusService.execute(command);

            // Then
            verify(sellerReadManager).findById(sellerId);
            verify(sellerPersistenceManager).persist(any(Seller.class));
        }

        @Test
        @DisplayName("APPROVED 상태에서 SUSPENDED로 변경 성공")
        void shouldSuspendFromApproved() {
            // Given
            Long sellerId = 1L;
            UpdateApprovalStatusCommand command =
                    UpdateApprovalStatusCommand.of(sellerId, "SUSPENDED");
            Seller approvedSeller = createApprovedSeller();

            when(sellerReadManager.findById(sellerId)).thenReturn(approvedSeller);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateApprovalStatusService.execute(command);

            // Then
            verify(sellerReadManager).findById(sellerId);
            verify(sellerPersistenceManager).persist(any(Seller.class));
        }

        @Test
        @DisplayName("PENDING 상태로의 변경은 저장되지 않는다")
        void shouldNotPersistWhenTargetIsPending() {
            // Given
            Long sellerId = 1L;
            UpdateApprovalStatusCommand command =
                    UpdateApprovalStatusCommand.of(sellerId, "PENDING");
            Seller pendingSeller = createPendingSeller();

            when(sellerReadManager.findById(sellerId)).thenReturn(pendingSeller);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateApprovalStatusService.execute(command);

            // Then
            verify(sellerReadManager).findById(sellerId);
            verify(sellerPersistenceManager, never()).persist(any(Seller.class));
        }
    }

    // ========== Helper Methods ==========

    private Seller createPendingSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("테스트 셀러"),
                null,
                null,
                ApprovalStatus.PENDING,
                createBusinessInfo(),
                createCustomerServiceInfo(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private Seller createApprovedSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("테스트 셀러"),
                null,
                null,
                ApprovalStatus.APPROVED,
                createBusinessInfo(),
                createCustomerServiceInfo(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private BusinessInfo createBusinessInfo() {
        return BusinessInfo.of(
                RegistrationNumber.of("1234567890"),
                SaleReportNumber.of("2024-서울강남-1234"),
                Representative.of("홍길동"),
                BusinessAddress.of("서울시 강남구", "테헤란로 123", "06234"));
    }

    private CustomerServiceInfo createCustomerServiceInfo() {
        return CustomerServiceInfo.of(
                CsEmail.of("cs@example.com"),
                CsMobilePhone.of("01012345678"),
                CsLandlinePhone.of("021234567"));
    }
}

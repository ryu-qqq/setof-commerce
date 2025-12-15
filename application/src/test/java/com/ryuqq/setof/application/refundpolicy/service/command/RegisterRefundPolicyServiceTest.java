package com.ryuqq.setof.application.refundpolicy.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.factory.command.RefundPolicyCommandFactory;
import com.ryuqq.setof.application.refundpolicy.manager.command.RefundPolicyPersistenceManager;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * RegisterRefundPolicyService 테스트
 *
 * <p>환불 정책 등록 서비스에 대한 단위 테스트
 */
@DisplayName("RegisterRefundPolicyService")
@ExtendWith(MockitoExtension.class)
class RegisterRefundPolicyServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Mock private RefundPolicyCommandFactory refundPolicyCommandFactory;
    @Mock private RefundPolicyPersistenceManager refundPolicyPersistenceManager;

    private RegisterRefundPolicyService registerRefundPolicyService;

    @BeforeEach
    void setUp() {
        registerRefundPolicyService =
                new RegisterRefundPolicyService(
                        refundPolicyCommandFactory, refundPolicyPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("환불 정책 등록 성공")
        void shouldRegisterRefundPolicySuccessfully() {
            // Given
            RegisterRefundPolicyCommand command = createCommand();
            RefundPolicy refundPolicy = createRefundPolicy();
            RefundPolicyId savedId = RefundPolicyId.of(1L);

            when(refundPolicyCommandFactory.create(any())).thenReturn(refundPolicy);
            when(refundPolicyPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerRefundPolicyService.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(1L, result);
            verify(refundPolicyCommandFactory).create(command);
            verify(refundPolicyPersistenceManager).persist(refundPolicy);
        }

        @Test
        @DisplayName("Factory와 Manager가 순서대로 호출된다")
        void shouldCallFactoryAndManagerInOrder() {
            // Given
            RegisterRefundPolicyCommand command = createCommand();
            RefundPolicy refundPolicy = createRefundPolicy();
            RefundPolicyId savedId = RefundPolicyId.of(100L);

            when(refundPolicyCommandFactory.create(any())).thenReturn(refundPolicy);
            when(refundPolicyPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerRefundPolicyService.execute(command);

            // Then
            assertEquals(100L, result);
            verify(refundPolicyCommandFactory).create(command);
            verify(refundPolicyPersistenceManager).persist(refundPolicy);
        }
    }

    // ========== Helper Methods ==========

    private RegisterRefundPolicyCommand createCommand() {
        return new RegisterRefundPolicyCommand(
                TEST_SELLER_ID,
                "기본 환불",
                "서울시 강남구",
                "테헤란로 123",
                "06234",
                7,
                3000,
                "상품 수령 후 7일 이내 환불 가능",
                true,
                1);
    }

    private RefundPolicy createRefundPolicy() {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("기본 환불"),
                ReturnAddress.of("서울시 강남구", "테헤란로 123", "06234"),
                RefundPeriodDays.of(7),
                RefundDeliveryCost.of(3000),
                RefundGuide.of("상품 수령 후 7일 이내 환불 가능"),
                true,
                1,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}

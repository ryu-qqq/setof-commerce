package com.ryuqq.setof.domain.claim;

import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.vo.ClaimId;
import com.ryuqq.setof.domain.claim.vo.ClaimNumber;
import com.ryuqq.setof.domain.claim.vo.ClaimReason;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.claim.vo.ClaimType;
import com.ryuqq.setof.domain.claim.vo.InspectionResult;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingMethod;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingStatus;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Claim TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Claim 인스턴스 생성을 위한 팩토리 클래스
 */
public final class ClaimFixture {

    private static final Instant DEFAULT_CREATED_AT = Instant.parse("2024-01-15T10:00:00Z");
    private static final Instant DEFAULT_UPDATED_AT = Instant.parse("2024-01-15T10:00:00Z");

    private ClaimFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 반품 클레임 생성 (REQUESTED 상태)
     *
     * @return Claim 인스턴스
     */
    public static Claim createReturnClaim() {
        return Claim.restore(
                1L,
                ClaimId.of("CLM-20240115-0001"),
                ClaimNumber.of("CLM-20240115-0001"),
                "ORD-20240110-0001",
                "ITEM-001",
                ClaimType.RETURN,
                ClaimReason.CHANGE_OF_MIND,
                "단순 변심으로 인한 반품 요청",
                1,
                BigDecimal.valueOf(50000),
                ClaimStatus.REQUESTED,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null,
                ReturnShippingStatus.PENDING,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * 교환 클레임 생성 (REQUESTED 상태)
     *
     * @return Claim 인스턴스
     */
    public static Claim createExchangeClaim() {
        return Claim.restore(
                2L,
                ClaimId.of("CLM-20240115-0002"),
                ClaimNumber.of("CLM-20240115-0002"),
                "ORD-20240110-0002",
                "ITEM-002",
                ClaimType.EXCHANGE,
                ClaimReason.SIZE_COLOR_MISMATCH,
                "사이즈가 맞지 않아 교환 요청",
                1,
                BigDecimal.ZERO,
                ClaimStatus.REQUESTED,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null,
                ReturnShippingStatus.PENDING,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * 취소 클레임 생성 (배송 전 취소)
     *
     * @return Claim 인스턴스
     */
    public static Claim createCancelClaim() {
        return Claim.restore(
                3L,
                ClaimId.of("CLM-20240115-0003"),
                ClaimNumber.of("CLM-20240115-0003"),
                "ORD-20240110-0003",
                null,
                ClaimType.CANCEL,
                ClaimReason.CHANGE_OF_MIND,
                "주문 취소 요청",
                null,
                BigDecimal.valueOf(100000),
                ClaimStatus.REQUESTED,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * 승인된 반품 클레임 생성
     *
     * @return Claim 인스턴스
     */
    public static Claim createApprovedReturnClaim() {
        return Claim.restore(
                4L,
                ClaimId.of("CLM-20240115-0004"),
                ClaimNumber.of("CLM-20240115-0004"),
                "ORD-20240110-0004",
                "ITEM-004",
                ClaimType.RETURN,
                ClaimReason.DEFECTIVE,
                "상품 불량",
                1,
                BigDecimal.valueOf(30000),
                ClaimStatus.APPROVED,
                "admin@example.com",
                DEFAULT_CREATED_AT.plusSeconds(3600),
                null,
                null,
                null,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null,
                ReturnShippingStatus.PENDING,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * 수거 예약된 반품 클레임 생성
     *
     * @return Claim 인스턴스
     */
    public static Claim createPickupScheduledClaim() {
        return Claim.restore(
                5L,
                ClaimId.of("CLM-20240115-0005"),
                ClaimNumber.of("CLM-20240115-0005"),
                "ORD-20240110-0005",
                "ITEM-005",
                ClaimType.RETURN,
                ClaimReason.CHANGE_OF_MIND,
                "단순 변심",
                1,
                BigDecimal.valueOf(25000),
                ClaimStatus.IN_PROGRESS,
                "admin@example.com",
                DEFAULT_CREATED_AT.plusSeconds(3600),
                null,
                null,
                null,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                ReturnShippingMethod.SELLER_PICKUP,
                ReturnShippingStatus.PICKUP_SCHEDULED,
                DEFAULT_CREATED_AT.plusSeconds(86400),
                "서울시 강남구 테헤란로 123",
                "010-1234-5678",
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * 반품 수령 완료된 클레임 생성
     *
     * @return Claim 인스턴스
     */
    public static Claim createReturnReceivedClaim() {
        return Claim.restore(
                6L,
                ClaimId.of("CLM-20240115-0006"),
                ClaimNumber.of("CLM-20240115-0006"),
                "ORD-20240110-0006",
                "ITEM-006",
                ClaimType.RETURN,
                ClaimReason.DEFECTIVE,
                "상품 불량으로 인한 반품",
                1,
                BigDecimal.valueOf(45000),
                ClaimStatus.IN_PROGRESS,
                "admin@example.com",
                DEFAULT_CREATED_AT.plusSeconds(3600),
                null,
                "1234567890",
                "CJ대한통운",
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                ReturnShippingMethod.CUSTOMER_SHIP,
                ReturnShippingStatus.RECEIVED,
                null,
                null,
                null,
                DEFAULT_CREATED_AT.plusSeconds(172800),
                null,
                null,
                null,
                null);
    }

    /**
     * 검수 완료된 반품 클레임 생성
     *
     * @return Claim 인스턴스
     */
    public static Claim createInspectedReturnClaim() {
        return Claim.restore(
                7L,
                ClaimId.of("CLM-20240115-0007"),
                ClaimNumber.of("CLM-20240115-0007"),
                "ORD-20240110-0007",
                "ITEM-007",
                ClaimType.RETURN,
                ClaimReason.DEFECTIVE,
                "상품 불량",
                1,
                BigDecimal.valueOf(55000),
                ClaimStatus.COMPLETED,
                "admin@example.com",
                DEFAULT_CREATED_AT.plusSeconds(3600),
                null,
                "9876543210",
                "한진택배",
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                ReturnShippingMethod.SELLER_PICKUP,
                ReturnShippingStatus.RECEIVED,
                DEFAULT_CREATED_AT.plusSeconds(86400),
                "서울시 강남구 테헤란로 456",
                "010-9876-5432",
                DEFAULT_CREATED_AT.plusSeconds(172800),
                InspectionResult.PASS,
                "검수 완료 - 이상 없음",
                null,
                null);
    }

    /**
     * 교환품 발송된 클레임 생성
     *
     * @return Claim 인스턴스
     */
    public static Claim createExchangeShippedClaim() {
        return Claim.restore(
                8L,
                ClaimId.of("CLM-20240115-0008"),
                ClaimNumber.of("CLM-20240115-0008"),
                "ORD-20240110-0008",
                "ITEM-008",
                ClaimType.EXCHANGE,
                ClaimReason.SIZE_COLOR_MISMATCH,
                "사이즈 교환",
                1,
                BigDecimal.ZERO,
                ClaimStatus.IN_PROGRESS,
                "admin@example.com",
                DEFAULT_CREATED_AT.plusSeconds(3600),
                null,
                "1111111111",
                "CJ대한통운",
                "2222222222",
                "CJ대한통운",
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                ReturnShippingMethod.SELLER_PICKUP,
                ReturnShippingStatus.RECEIVED,
                DEFAULT_CREATED_AT.plusSeconds(86400),
                "서울시 서초구 서초대로 789",
                "010-1111-2222",
                DEFAULT_CREATED_AT.plusSeconds(172800),
                InspectionResult.PASS,
                "검수 합격",
                DEFAULT_CREATED_AT.plusSeconds(259200),
                null);
    }

    /**
     * 교환품 발송 대기 상태의 교환 클레임 생성 (반품 수령 완료 + 검수 합격 상태)
     *
     * @return Claim 인스턴스
     */
    public static Claim createCustomExchange() {
        return Claim.restore(
                9L,
                ClaimId.of("CLM-20240115-0009"),
                ClaimNumber.of("CLM-20240115-0009"),
                "ORD-20240110-0009",
                "ITEM-009",
                ClaimType.EXCHANGE,
                ClaimReason.SIZE_COLOR_MISMATCH,
                "사이즈 교환 요청",
                1,
                BigDecimal.ZERO,
                ClaimStatus.IN_PROGRESS,
                "admin@example.com",
                DEFAULT_CREATED_AT.plusSeconds(3600),
                null,
                "3333333333",
                "CJ대한통운",
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                ReturnShippingMethod.CUSTOMER_SHIP,
                ReturnShippingStatus.RECEIVED,
                null,
                null,
                null,
                DEFAULT_CREATED_AT.plusSeconds(172800),
                InspectionResult.PASS,
                "검수 합격 - 교환품 발송 대기",
                null,
                null);
    }

    /**
     * 커스텀 클레임 생성
     *
     * @param id DB ID
     * @param claimId 클레임 ID
     * @param orderId 주문 ID
     * @param claimType 클레임 유형
     * @param status 클레임 상태
     * @param shippingStatus 반품 배송 상태
     * @return Claim 인스턴스
     */
    public static Claim createCustom(
            Long id,
            String claimId,
            String orderId,
            ClaimType claimType,
            ClaimStatus status,
            ReturnShippingStatus shippingStatus) {
        return Claim.restore(
                id,
                ClaimId.of(claimId),
                ClaimNumber.of(claimId),
                orderId,
                "ITEM-CUSTOM",
                claimType,
                ClaimReason.CHANGE_OF_MIND,
                "테스트 클레임",
                1,
                BigDecimal.valueOf(10000),
                status,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null,
                shippingStatus,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}

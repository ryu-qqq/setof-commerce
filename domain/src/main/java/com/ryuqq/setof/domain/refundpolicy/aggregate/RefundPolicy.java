package com.ryuqq.setof.domain.refundpolicy.aggregate;

import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;

/**
 * RefundPolicy Aggregate Root
 *
 * <p>반품/교환 정책을 나타내는 도메인 엔티티입니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>셀러당 최소 1개의 반품 정책이 필수
 *   <li>기본 정책(isDefault)은 반드시 1개 존재
 *   <li>기본 정책 삭제 시 가장 최근 정책이 자동 승격
 *   <li>마지막 정책은 삭제 불가
 *   <li>반품 기간 기본값: 7일, 최대 365일
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
public class RefundPolicy {

    private final RefundPolicyId id;
    private final Long sellerId;
    private final PolicyName policyName;
    private final ReturnAddress returnAddress;
    private final RefundPeriodDays refundPeriodDays;
    private final RefundDeliveryCost refundDeliveryCost;
    private final RefundGuide refundGuide;
    private final boolean isDefault;
    private final Integer displayOrder;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private RefundPolicy(
            RefundPolicyId id,
            Long sellerId,
            PolicyName policyName,
            ReturnAddress returnAddress,
            RefundPeriodDays refundPeriodDays,
            RefundDeliveryCost refundDeliveryCost,
            RefundGuide refundGuide,
            boolean isDefault,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.policyName = policyName;
        this.returnAddress = returnAddress;
        this.refundPeriodDays = refundPeriodDays;
        this.refundDeliveryCost = refundDeliveryCost;
        this.refundGuide = refundGuide;
        this.isDefault = isDefault;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 반품 정책 생성용 Static Factory Method
     *
     * @param sellerId 셀러 ID
     * @param policyName 정책명
     * @param returnAddress 반품 주소
     * @param refundPeriodDays 반품 가능 기간 (일)
     * @param refundDeliveryCost 반품 배송비
     * @param refundGuide 반품 안내 (nullable)
     * @param isDefault 기본 정책 여부
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @return RefundPolicy 인스턴스
     */
    public static RefundPolicy create(
            Long sellerId,
            PolicyName policyName,
            ReturnAddress returnAddress,
            RefundPeriodDays refundPeriodDays,
            RefundDeliveryCost refundDeliveryCost,
            RefundGuide refundGuide,
            boolean isDefault,
            Integer displayOrder,
            Instant createdAt) {
        return new RefundPolicy(
                null,
                sellerId,
                policyName,
                returnAddress,
                refundPeriodDays,
                refundDeliveryCost,
                refundGuide,
                isDefault,
                displayOrder,
                createdAt,
                createdAt,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 반품 정책 ID
     * @param sellerId 셀러 ID
     * @param policyName 정책명
     * @param returnAddress 반품 주소
     * @param refundPeriodDays 반품 가능 기간 (일)
     * @param refundDeliveryCost 반품 배송비
     * @param refundGuide 반품 안내 (nullable)
     * @param isDefault 기본 정책 여부
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시 (nullable)
     * @return RefundPolicy 인스턴스
     */
    public static RefundPolicy reconstitute(
            RefundPolicyId id,
            Long sellerId,
            PolicyName policyName,
            ReturnAddress returnAddress,
            RefundPeriodDays refundPeriodDays,
            RefundDeliveryCost refundDeliveryCost,
            RefundGuide refundGuide,
            boolean isDefault,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new RefundPolicy(
                id,
                sellerId,
                policyName,
                returnAddress,
                refundPeriodDays,
                refundDeliveryCost,
                refundGuide,
                isDefault,
                displayOrder,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 반품 정책 업데이트
     *
     * @param policyName 새로운 정책명
     * @param returnAddress 새로운 반품 주소
     * @param refundPeriodDays 새로운 반품 가능 기간
     * @param refundDeliveryCost 새로운 반품 배송비
     * @param refundGuide 새로운 반품 안내
     * @param updatedAt 수정일시
     * @return 업데이트된 RefundPolicy 인스턴스
     */
    public RefundPolicy update(
            PolicyName policyName,
            ReturnAddress returnAddress,
            RefundPeriodDays refundPeriodDays,
            RefundDeliveryCost refundDeliveryCost,
            RefundGuide refundGuide,
            Instant updatedAt) {
        return new RefundPolicy(
                this.id,
                this.sellerId,
                policyName,
                returnAddress,
                refundPeriodDays,
                refundDeliveryCost,
                refundGuide,
                this.isDefault,
                this.displayOrder,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 기본 정책으로 설정
     *
     * @param updatedAt 수정일시
     * @return 기본 정책으로 설정된 RefundPolicy 인스턴스
     */
    public RefundPolicy setAsDefault(Instant updatedAt) {
        return new RefundPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.returnAddress,
                this.refundPeriodDays,
                this.refundDeliveryCost,
                this.refundGuide,
                true,
                this.displayOrder,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 기본 정책 해제
     *
     * @param updatedAt 수정일시
     * @return 기본 정책이 해제된 RefundPolicy 인스턴스
     */
    public RefundPolicy unsetDefault(Instant updatedAt) {
        return new RefundPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.returnAddress,
                this.refundPeriodDays,
                this.refundDeliveryCost,
                this.refundGuide,
                false,
                this.displayOrder,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 삭제 처리 (Soft Delete)
     *
     * @param deletedAt 삭제일시
     * @return 삭제된 RefundPolicy 인스턴스
     */
    public RefundPolicy delete(Instant deletedAt) {
        return new RefundPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.returnAddress,
                this.refundPeriodDays,
                this.refundDeliveryCost,
                this.refundGuide,
                false, // 삭제 시 기본 정책 해제
                this.displayOrder,
                this.createdAt,
                deletedAt,
                deletedAt);
    }

    /**
     * 반품 가능 여부 확인
     *
     * @param orderDate 주문 일시
     * @param currentDate 현재 일시
     * @return 반품 가능 기간 내이면 true
     */
    public boolean isWithinRefundPeriod(Instant orderDate, Instant currentDate) {
        return refundPeriodDays.isWithinPeriod(orderDate, currentDate);
    }

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null;
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 반품 정책 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 반품 정책 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 정책명 값 반환 (Law of Demeter 준수)
     *
     * @return 정책명
     */
    public String getPolicyNameValue() {
        return policyName.value();
    }

    /**
     * 반품 주소 (주소1) 값 반환 (Law of Demeter 준수)
     *
     * @return 반품 주소 addressLine1
     */
    public String getReturnAddressLine1() {
        return returnAddress.addressLine1();
    }

    /**
     * 반품 주소 (주소2) 값 반환 (Law of Demeter 준수)
     *
     * @return 반품 주소 addressLine2, 없으면 null
     */
    public String getReturnAddressLine2() {
        return returnAddress.addressLine2();
    }

    /**
     * 반품 주소 우편번호 값 반환 (Law of Demeter 준수)
     *
     * @return 우편번호
     */
    public String getReturnZipCode() {
        return returnAddress.zipCode();
    }

    /**
     * 반품 가능 기간 (일) 값 반환 (Law of Demeter 준수)
     *
     * @return 반품 가능 기간 (일)
     */
    public Integer getRefundPeriodDaysValue() {
        return refundPeriodDays.value();
    }

    /**
     * 반품 배송비 값 반환 (Law of Demeter 준수)
     *
     * @return 반품 배송비 (원)
     */
    public Integer getRefundDeliveryCostValue() {
        return refundDeliveryCost.value();
    }

    /**
     * 반품 안내 값 반환 (Law of Demeter 준수)
     *
     * @return 반품 안내 문구, 없으면 null
     */
    public String getRefundGuideValue() {
        return refundGuide != null ? refundGuide.value() : null;
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public RefundPolicyId getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public PolicyName getPolicyName() {
        return policyName;
    }

    public ReturnAddress getReturnAddress() {
        return returnAddress;
    }

    public RefundPeriodDays getRefundPeriodDays() {
        return refundPeriodDays;
    }

    public RefundDeliveryCost getRefundDeliveryCost() {
        return refundDeliveryCost;
    }

    public RefundGuide getRefundGuide() {
        return refundGuide;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}

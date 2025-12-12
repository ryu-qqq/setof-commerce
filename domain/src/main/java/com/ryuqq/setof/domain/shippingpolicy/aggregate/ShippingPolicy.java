package com.ryuqq.setof.domain.shippingpolicy.aggregate;

import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;

/**
 * ShippingPolicy Aggregate Root
 *
 * <p>배송 정책을 나타내는 도메인 엔티티입니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>셀러당 최소 1개의 배송 정책이 필수
 *   <li>기본 정책(isDefault)은 반드시 1개 존재
 *   <li>기본 정책 삭제 시 가장 최근 정책이 자동 승격
 *   <li>마지막 정책은 삭제 불가
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
public class ShippingPolicy {

    private final ShippingPolicyId id;
    private final Long sellerId;
    private final PolicyName policyName;
    private final DeliveryCost defaultDeliveryCost;
    private final FreeShippingThreshold freeShippingThreshold;
    private final DeliveryGuide deliveryGuide;
    private final boolean isDefault;
    private final DisplayOrder displayOrder;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private ShippingPolicy(
            ShippingPolicyId id,
            Long sellerId,
            PolicyName policyName,
            DeliveryCost defaultDeliveryCost,
            FreeShippingThreshold freeShippingThreshold,
            DeliveryGuide deliveryGuide,
            boolean isDefault,
            DisplayOrder displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.policyName = policyName;
        this.defaultDeliveryCost = defaultDeliveryCost;
        this.freeShippingThreshold = freeShippingThreshold;
        this.deliveryGuide = deliveryGuide;
        this.isDefault = isDefault;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 배송 정책 생성용 Static Factory Method
     *
     * @param sellerId 셀러 ID
     * @param policyName 정책명
     * @param defaultDeliveryCost 기본 배송비
     * @param freeShippingThreshold 무료 배송 기준 금액 (nullable)
     * @param deliveryGuide 배송 안내 (nullable)
     * @param isDefault 기본 정책 여부
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @return ShippingPolicy 인스턴스
     */
    public static ShippingPolicy create(
            Long sellerId,
            PolicyName policyName,
            DeliveryCost defaultDeliveryCost,
            FreeShippingThreshold freeShippingThreshold,
            DeliveryGuide deliveryGuide,
            boolean isDefault,
            DisplayOrder displayOrder,
            Instant createdAt) {
        return new ShippingPolicy(
                null,
                sellerId,
                policyName,
                defaultDeliveryCost,
                freeShippingThreshold,
                deliveryGuide,
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
     * @param id 배송 정책 ID
     * @param sellerId 셀러 ID
     * @param policyName 정책명
     * @param defaultDeliveryCost 기본 배송비
     * @param freeShippingThreshold 무료 배송 기준 금액 (nullable)
     * @param deliveryGuide 배송 안내 (nullable)
     * @param isDefault 기본 정책 여부
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시 (nullable)
     * @return ShippingPolicy 인스턴스
     */
    public static ShippingPolicy reconstitute(
            ShippingPolicyId id,
            Long sellerId,
            PolicyName policyName,
            DeliveryCost defaultDeliveryCost,
            FreeShippingThreshold freeShippingThreshold,
            DeliveryGuide deliveryGuide,
            boolean isDefault,
            DisplayOrder displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ShippingPolicy(
                id,
                sellerId,
                policyName,
                defaultDeliveryCost,
                freeShippingThreshold,
                deliveryGuide,
                isDefault,
                displayOrder,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 배송 정책 업데이트
     *
     * @param policyName 새로운 정책명
     * @param defaultDeliveryCost 새로운 기본 배송비
     * @param freeShippingThreshold 새로운 무료 배송 기준 금액
     * @param deliveryGuide 새로운 배송 안내
     * @param displayOrder 새로운 표시 순서
     * @param updatedAt 수정일시
     * @return 업데이트된 ShippingPolicy 인스턴스
     */
    public ShippingPolicy update(
            PolicyName policyName,
            DeliveryCost defaultDeliveryCost,
            FreeShippingThreshold freeShippingThreshold,
            DeliveryGuide deliveryGuide,
            DisplayOrder displayOrder,
            Instant updatedAt) {
        return new ShippingPolicy(
                this.id,
                this.sellerId,
                policyName,
                defaultDeliveryCost,
                freeShippingThreshold,
                deliveryGuide,
                this.isDefault,
                displayOrder,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 기본 정책으로 설정
     *
     * @param updatedAt 수정일시
     * @return 기본 정책으로 설정된 ShippingPolicy 인스턴스
     */
    public ShippingPolicy setAsDefault(Instant updatedAt) {
        return new ShippingPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.defaultDeliveryCost,
                this.freeShippingThreshold,
                this.deliveryGuide,
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
     * @return 기본 정책이 해제된 ShippingPolicy 인스턴스
     */
    public ShippingPolicy unsetDefault(Instant updatedAt) {
        return new ShippingPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.defaultDeliveryCost,
                this.freeShippingThreshold,
                this.deliveryGuide,
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
     * @return 삭제된 ShippingPolicy 인스턴스
     */
    public ShippingPolicy delete(Instant deletedAt) {
        return new ShippingPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.defaultDeliveryCost,
                this.freeShippingThreshold,
                this.deliveryGuide,
                false, // 삭제 시 기본 정책 해제
                this.displayOrder,
                this.createdAt,
                deletedAt,
                deletedAt);
    }

    /**
     * 배송비 계산
     *
     * @param orderAmount 주문 금액
     * @return 적용될 배송비
     */
    public int calculateDeliveryCost(int orderAmount) {
        if (freeShippingThreshold != null && freeShippingThreshold.isFreeShipping(orderAmount)) {
            return 0;
        }
        return defaultDeliveryCost.value();
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
     * 배송 정책 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 배송 정책 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 정책명 값 반환 (Law of Demeter 준수)
     *
     * @return 정책명 문자열
     */
    public String getPolicyNameValue() {
        return policyName.value();
    }

    /**
     * 기본 배송비 값 반환 (Law of Demeter 준수)
     *
     * @return 기본 배송비 (원)
     */
    public Integer getDefaultDeliveryCostValue() {
        return defaultDeliveryCost.value();
    }

    /**
     * 무료 배송 기준 금액 값 반환 (Law of Demeter 준수)
     *
     * @return 무료 배송 기준 금액 (원), 없으면 null
     */
    public Integer getFreeShippingThresholdValue() {
        return freeShippingThreshold != null ? freeShippingThreshold.value() : null;
    }

    /**
     * 배송 안내 값 반환 (Law of Demeter 준수)
     *
     * @return 배송 안내 문구, 없으면 null
     */
    public String getDeliveryGuideValue() {
        return deliveryGuide != null ? deliveryGuide.value() : null;
    }

    /**
     * 표시 순서 값 반환 (Law of Demeter 준수)
     *
     * @return 표시 순서
     */
    public Integer getDisplayOrderValue() {
        return displayOrder.value();
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ShippingPolicyId getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public PolicyName getPolicyName() {
        return policyName;
    }

    public DeliveryCost getDefaultDeliveryCost() {
        return defaultDeliveryCost;
    }

    public FreeShippingThreshold getFreeShippingThreshold() {
        return freeShippingThreshold;
    }

    public DeliveryGuide getDeliveryGuide() {
        return deliveryGuide;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public DisplayOrder getDisplayOrder() {
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

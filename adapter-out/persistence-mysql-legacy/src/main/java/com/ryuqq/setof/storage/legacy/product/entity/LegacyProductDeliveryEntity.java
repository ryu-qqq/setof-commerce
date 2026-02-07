package com.ryuqq.setof.storage.legacy.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyProductDeliveryEntity - 레거시 상품 배송 정보 엔티티.
 *
 * <p>레거시 DB의 product_delivery 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_delivery")
public class LegacyProductDeliveryEntity {

    @Id
    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "delivery_notice", columnDefinition = "TEXT")
    private String deliveryNotice;

    @Column(name = "refund_notice", columnDefinition = "TEXT")
    private String refundNotice;

    @Column(name = "delivery_fee")
    private Integer deliveryFee;

    @Column(name = "free_delivery_threshold")
    private Integer freeDeliveryThreshold;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected LegacyProductDeliveryEntity() {}

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getDeliveryNotice() {
        return deliveryNotice;
    }

    public String getRefundNotice() {
        return refundNotice;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public Integer getFreeDeliveryThreshold() {
        return freeDeliveryThreshold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

package com.ryuqq.setof.storage.legacy.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyProductOptionEntity - 레거시 상품 옵션 엔티티.
 *
 * <p>레거시 DB의 product_option 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_option")
public class LegacyProductOptionEntity {

    @Id
    @Column(name = "product_option_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "option_group_id")
    private Long optionGroupId;

    @Column(name = "option_detail_id")
    private Long optionDetailId;

    @Column(name = "additional_price")
    private Long additionalPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected LegacyProductOptionEntity() {}

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionGroupId() {
        return optionGroupId;
    }

    public Long getOptionDetailId() {
        return optionDetailId;
    }

    public Long getAdditionalPrice() {
        return additionalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

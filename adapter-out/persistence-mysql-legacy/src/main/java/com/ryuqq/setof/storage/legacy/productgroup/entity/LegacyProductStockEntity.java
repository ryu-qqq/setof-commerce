package com.ryuqq.setof.storage.legacy.productgroup.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyProductStockEntity - 레거시 상품 재고 엔티티.
 *
 * <p>레거시 DB의 product_stock 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_stock")
public class LegacyProductStockEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    /** Added for productgroup composite support. 재고 삭제 여부 필터링. */
    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyProductStockEntity() {}

    public Long getProductId() {
        return productId;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}

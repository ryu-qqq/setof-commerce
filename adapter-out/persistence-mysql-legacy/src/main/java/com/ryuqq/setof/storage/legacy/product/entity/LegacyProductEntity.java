package com.ryuqq.setof.storage.legacy.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyProductEntity - 레거시 상품 엔티티.
 *
 * <p>레거시 DB의 product 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product")
public class LegacyProductEntity {

    @Id
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "sold_out_yn")
    @Enumerated(EnumType.STRING)
    private Yn soldOutYn;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected LegacyProductEntity() {}

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public Yn getSoldOutYn() {
        return soldOutYn;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 상품 상태를 문자열로 반환.
     *
     * @return 상품 상태 (ON_SALE, SOLD_OUT, HIDDEN)
     */
    public String getProductStatus() {
        if (soldOutYn == Yn.Y) {
            return "SOLD_OUT";
        }
        if (displayYn == Yn.N) {
            return "HIDDEN";
        }
        return "ON_SALE";
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}

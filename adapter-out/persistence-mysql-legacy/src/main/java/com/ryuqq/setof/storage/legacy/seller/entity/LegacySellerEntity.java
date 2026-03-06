package com.ryuqq.setof.storage.legacy.seller.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacySellerEntity - 레거시 판매자 엔티티.
 *
 * <p>레거시 DB의 seller 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "seller")
public class LegacySellerEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "seller_logo_url")
    private String sellerLogoUrl;

    @Column(name = "seller_description")
    private String sellerDescription;

    @Column(name = "commission_rate")
    private Double commissionRate;

    protected LegacySellerEntity() {}

    public Long getId() {
        return id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerLogoUrl() {
        return sellerLogoUrl;
    }

    public String getSellerDescription() {
        return sellerDescription;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }
}

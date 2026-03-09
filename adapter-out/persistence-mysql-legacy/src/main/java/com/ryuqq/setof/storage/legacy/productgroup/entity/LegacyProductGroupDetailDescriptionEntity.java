package com.ryuqq.setof.storage.legacy.productgroup.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyProductGroupDetailDescriptionEntity - 레거시 상품그룹 상세 설명 엔티티.
 *
 * <p>레거시 DB의 product_group_detail_description 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_group_detail_description")
public class LegacyProductGroupDetailDescriptionEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyProductGroupDetailDescriptionEntity() {}

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getImageUrl() {
        return imageUrl;
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

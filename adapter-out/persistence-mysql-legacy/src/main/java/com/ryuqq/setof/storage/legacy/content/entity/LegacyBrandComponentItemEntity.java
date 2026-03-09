package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyBrandComponentItemEntity - 레거시 브랜드 컴포넌트 아이템 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "brand_component_item")
public class LegacyBrandComponentItemEntity {

    @Id
    @Column(name = "brand_component_item_id")
    private Long id;

    @Column(name = "brand_component_id")
    private long brandComponentId;

    @Column(name = "brand_id")
    private long brandId;

    @Column(name = "category_id")
    private long categoryId;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyBrandComponentItemEntity() {}

    public Long getId() {
        return id;
    }

    public long getBrandComponentId() {
        return brandComponentId;
    }

    public long getBrandId() {
        return brandId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

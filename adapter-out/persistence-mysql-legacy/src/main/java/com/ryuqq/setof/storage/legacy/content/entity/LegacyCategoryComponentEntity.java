package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyCategoryComponentEntity - 레거시 카테고리 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "category_component")
public class LegacyCategoryComponentEntity {

    @Id
    @Column(name = "category_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "category_id")
    private long categoryId;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyCategoryComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

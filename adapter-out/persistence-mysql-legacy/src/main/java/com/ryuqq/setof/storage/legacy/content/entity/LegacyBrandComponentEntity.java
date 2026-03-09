package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyBrandComponentEntity - 레거시 브랜드 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "brand_component")
public class LegacyBrandComponentEntity {

    @Id
    @Column(name = "brand_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyBrandComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyProductComponentEntity - 레거시 상품 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_component")
public class LegacyProductComponentEntity {

    @Id
    @Column(name = "product_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyProductComponentEntity() {}

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

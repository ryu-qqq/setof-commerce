package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyTabComponentEntity - 레거시 탭 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "tab_component")
public class LegacyTabComponentEntity {

    @Id
    @Column(name = "tab_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "sticky_yn")
    private String stickyYn;

    @Column(name = "tab_moving_type")
    private String tabMovingType;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyTabComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public String getStickyYn() {
        return stickyYn;
    }

    public String getTabMovingType() {
        return tabMovingType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

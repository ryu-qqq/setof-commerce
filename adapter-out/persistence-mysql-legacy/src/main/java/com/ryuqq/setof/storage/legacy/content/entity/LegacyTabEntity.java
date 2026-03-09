package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyTabEntity - 레거시 탭 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "tab")
public class LegacyTabEntity {

    @Id
    @Column(name = "tab_id")
    private Long id;

    @Column(name = "tab_name")
    private String tabName;

    @Column(name = "tab_component_id")
    private long tabComponentId;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyTabEntity() {}

    public Long getId() {
        return id;
    }

    public String getTabName() {
        return tabName;
    }

    public long getTabComponentId() {
        return tabComponentId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

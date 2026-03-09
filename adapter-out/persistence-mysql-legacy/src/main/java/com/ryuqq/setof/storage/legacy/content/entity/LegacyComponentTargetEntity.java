package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyComponentTargetEntity - 레거시 컴포넌트 타겟 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "component_target")
public class LegacyComponentTargetEntity {

    @Id
    @Column(name = "component_target_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "tab_id")
    private long tabId;

    @Column(name = "sort_type")
    private String sortType;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyComponentTargetEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public long getTabId() {
        return tabId;
    }

    public String getSortType() {
        return sortType;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

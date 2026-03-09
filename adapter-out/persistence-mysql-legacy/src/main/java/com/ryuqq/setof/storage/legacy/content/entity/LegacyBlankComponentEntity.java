package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyBlankComponentEntity - 레거시 여백 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "blank_component")
public class LegacyBlankComponentEntity {

    @Id
    @Column(name = "blank_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "height")
    private double height;

    @Column(name = "line_yn")
    private String lineYn;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyBlankComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public double getHeight() {
        return height;
    }

    public String getLineYn() {
        return lineYn;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

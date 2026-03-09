package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyTextComponentEntity - 레거시 텍스트 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "text_component")
public class LegacyTextComponentEntity {

    @Id
    @Column(name = "text_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "content")
    private String content;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyTextComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public String getContent() {
        return content;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

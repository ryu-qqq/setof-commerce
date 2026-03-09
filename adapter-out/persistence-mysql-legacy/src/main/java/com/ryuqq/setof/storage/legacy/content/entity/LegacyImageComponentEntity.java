package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyImageComponentEntity - 레거시 이미지 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "image_component")
public class LegacyImageComponentEntity {

    @Id
    @Column(name = "image_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyImageComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public String getImageType() {
        return imageType;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

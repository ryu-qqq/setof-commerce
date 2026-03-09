package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyImageComponentItemEntity - 레거시 이미지 컴포넌트 아이템 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "image_component_item")
public class LegacyImageComponentItemEntity {

    @Id
    @Column(name = "image_component_item_id")
    private Long id;

    @Column(name = "image_component_id")
    private long imageComponentId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "width")
    private long width;

    @Column(name = "height")
    private long height;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyImageComponentItemEntity() {}

    public Long getId() {
        return id;
    }

    public long getImageComponentId() {
        return imageComponentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

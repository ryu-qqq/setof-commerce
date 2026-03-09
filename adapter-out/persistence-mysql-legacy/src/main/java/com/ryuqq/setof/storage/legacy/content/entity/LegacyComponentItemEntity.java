package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyComponentItemEntity - 레거시 컴포넌트 아이템 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "component_item")
public class LegacyComponentItemEntity {

    @Id
    @Column(name = "component_item_id")
    private Long id;

    @Column(name = "component_target_id")
    private long componentTargetId;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "product_display_name")
    private String productDisplayName;

    @Column(name = "product_display_image")
    private String productDisplayImage;

    @Column(name = "display_order")
    private Long displayOrder;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyComponentItemEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentTargetId() {
        return componentTargetId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public String getProductDisplayImage() {
        return productDisplayImage;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}

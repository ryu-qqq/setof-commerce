package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyViewExtensionEntity - 레거시 뷰 확장 엔티티.
 *
 * <p>레거시 DB의 view_extension 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "view_extension")
public class LegacyViewExtensionEntity {

    @Id
    @Column(name = "view_extension_id")
    private Long id;

    @Column(name = "view_extension_type")
    @Enumerated(EnumType.STRING)
    private ViewExtensionType viewExtensionType;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "button_name")
    private String buttonName;

    @Column(name = "product_count_per_click")
    private int productCountPerClick;

    @Column(name = "max_click_count")
    private int maxClickCount;

    @Column(name = "after_max_action_type")
    @Enumerated(EnumType.STRING)
    private ViewExtensionType afterMaxActionType;

    @Column(name = "after_max_action_link_url")
    private String afterMaxActionLinkUrl;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyViewExtensionEntity() {}

    public Long getId() {
        return id;
    }

    public ViewExtensionType getViewExtensionType() {
        return viewExtensionType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getButtonName() {
        return buttonName;
    }

    public int getProductCountPerClick() {
        return productCountPerClick;
    }

    public int getMaxClickCount() {
        return maxClickCount;
    }

    public ViewExtensionType getAfterMaxActionType() {
        return afterMaxActionType;
    }

    public String getAfterMaxActionLinkUrl() {
        return afterMaxActionLinkUrl;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** ViewExtensionType - 뷰 확장 타입 Enum. */
    public enum ViewExtensionType {
        PAGE,
        LINKING,
        PRODUCT,
        SCROLL,
        NONE
    }
}

package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyComponentEntity - 레거시 컴포넌트 엔티티.
 *
 * <p>레거시 DB의 component 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "component")
public class LegacyComponentEntity {

    @Id
    @Column(name = "component_id")
    private Long id;

    @Column(name = "content_id")
    private long contentId;

    @Column(name = "component_name")
    private String componentName;

    @Column(name = "component_type")
    @Enumerated(EnumType.STRING)
    private ComponentType componentType;

    @Column(name = "list_type")
    @Enumerated(EnumType.STRING)
    private ListType listType;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "badge_type")
    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    @Column(name = "filter_yn")
    @Enumerated(EnumType.STRING)
    private Yn filterYn;

    @Column(name = "exposed_products")
    private int exposedProducts;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "view_extension_id")
    private Long viewExtensionId;

    @Column(name = "display_start_date")
    private LocalDateTime displayStartDate;

    @Column(name = "display_end_date")
    private LocalDateTime displayEndDate;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getContentId() {
        return contentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public ListType getListType() {
        return listType;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public BadgeType getBadgeType() {
        return badgeType;
    }

    public Yn getFilterYn() {
        return filterYn;
    }

    public int getExposedProducts() {
        return exposedProducts;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public Long getViewExtensionId() {
        return viewExtensionId;
    }

    public LocalDateTime getDisplayStartDate() {
        return displayStartDate;
    }

    public LocalDateTime getDisplayEndDate() {
        return displayEndDate;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** ComponentType - 컴포넌트 타입 Enum. */
    public enum ComponentType {
        TEXT,
        TITLE,
        IMAGE,
        BLANK,
        TAB,
        BRAND,
        CATEGORY,
        PRODUCT
    }

    /** ListType - 리스트 표시 형식 Enum. */
    public enum ListType {
        ONE_STEP,
        TWO_STEP,
        MULTI,
        COLUMN,
        ROW,
        NONE
    }

    /** OrderType - 정렬 방식 Enum. */
    public enum OrderType {
        NONE,
        RECOMMEND,
        REVIEW,
        RECENT,
        HIGH_RATING,
        LOW_PRICE,
        HIGH_PRICE,
        LOW_DISCOUNT,
        HIGH_DISCOUNT
    }

    /** BadgeType - 배지 타입 Enum. */
    public enum BadgeType {
        LIKE,
        RANKING,
        ALL,
        NONE
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}

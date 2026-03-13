package com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "display_component")
public class DisplayComponentJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_page_id", nullable = false)
    private long contentPageId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "component_type", nullable = false, length = 30)
    private String componentType;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "list_type", nullable = false, length = 20)
    private String listType;

    @Column(name = "order_type", nullable = false, length = 20)
    private String orderType;

    @Column(name = "badge_type", nullable = false, length = 20)
    private String badgeType;

    @Column(name = "filter_enabled", nullable = false)
    private boolean filterEnabled;

    @Column(name = "exposed_products", nullable = false)
    private int exposedProducts;

    @Column(name = "display_start_at", nullable = false)
    private Instant displayStartAt;

    @Column(name = "display_end_at", nullable = false)
    private Instant displayEndAt;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "view_extension_type", length = 20)
    private String viewExtensionType;

    @Column(name = "view_extension_link_url", length = 500)
    private String viewExtensionLinkUrl;

    @Column(name = "view_extension_button_name", length = 100)
    private String viewExtensionButtonName;

    @Column(name = "view_extension_product_count_per_click")
    private Integer viewExtensionProductCountPerClick;

    @Column(name = "view_extension_max_click_count")
    private Integer viewExtensionMaxClickCount;

    @Column(name = "view_extension_after_max_action_type", length = 20)
    private String viewExtensionAfterMaxActionType;

    @Column(name = "view_extension_after_max_action_link_url", length = 500)
    private String viewExtensionAfterMaxActionLinkUrl;

    @Column(name = "spec_data", columnDefinition = "json")
    private String specData;

    protected DisplayComponentJpaEntity() {
        super();
    }

    private DisplayComponentJpaEntity(
            Long id,
            long contentPageId,
            String name,
            String componentType,
            int displayOrder,
            String listType,
            String orderType,
            String badgeType,
            boolean filterEnabled,
            int exposedProducts,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            String viewExtensionType,
            String viewExtensionLinkUrl,
            String viewExtensionButtonName,
            Integer viewExtensionProductCountPerClick,
            Integer viewExtensionMaxClickCount,
            String viewExtensionAfterMaxActionType,
            String viewExtensionAfterMaxActionLinkUrl,
            String specData,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.contentPageId = contentPageId;
        this.name = name;
        this.componentType = componentType;
        this.displayOrder = displayOrder;
        this.listType = listType;
        this.orderType = orderType;
        this.badgeType = badgeType;
        this.filterEnabled = filterEnabled;
        this.exposedProducts = exposedProducts;
        this.displayStartAt = displayStartAt;
        this.displayEndAt = displayEndAt;
        this.active = active;
        this.viewExtensionType = viewExtensionType;
        this.viewExtensionLinkUrl = viewExtensionLinkUrl;
        this.viewExtensionButtonName = viewExtensionButtonName;
        this.viewExtensionProductCountPerClick = viewExtensionProductCountPerClick;
        this.viewExtensionMaxClickCount = viewExtensionMaxClickCount;
        this.viewExtensionAfterMaxActionType = viewExtensionAfterMaxActionType;
        this.viewExtensionAfterMaxActionLinkUrl = viewExtensionAfterMaxActionLinkUrl;
        this.specData = specData;
    }

    public static DisplayComponentJpaEntity create(
            Long id,
            long contentPageId,
            String name,
            String componentType,
            int displayOrder,
            String listType,
            String orderType,
            String badgeType,
            boolean filterEnabled,
            int exposedProducts,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            String viewExtensionType,
            String viewExtensionLinkUrl,
            String viewExtensionButtonName,
            Integer viewExtensionProductCountPerClick,
            Integer viewExtensionMaxClickCount,
            String viewExtensionAfterMaxActionType,
            String viewExtensionAfterMaxActionLinkUrl,
            String specData,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new DisplayComponentJpaEntity(
                id,
                contentPageId,
                name,
                componentType,
                displayOrder,
                listType,
                orderType,
                badgeType,
                filterEnabled,
                exposedProducts,
                displayStartAt,
                displayEndAt,
                active,
                viewExtensionType,
                viewExtensionLinkUrl,
                viewExtensionButtonName,
                viewExtensionProductCountPerClick,
                viewExtensionMaxClickCount,
                viewExtensionAfterMaxActionType,
                viewExtensionAfterMaxActionLinkUrl,
                specData,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public long getContentPageId() {
        return contentPageId;
    }

    public String getName() {
        return name;
    }

    public String getComponentType() {
        return componentType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getListType() {
        return listType;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getBadgeType() {
        return badgeType;
    }

    public boolean isFilterEnabled() {
        return filterEnabled;
    }

    public int getExposedProducts() {
        return exposedProducts;
    }

    public Instant getDisplayStartAt() {
        return displayStartAt;
    }

    public Instant getDisplayEndAt() {
        return displayEndAt;
    }

    public boolean isActive() {
        return active;
    }

    public String getViewExtensionType() {
        return viewExtensionType;
    }

    public String getViewExtensionLinkUrl() {
        return viewExtensionLinkUrl;
    }

    public String getViewExtensionButtonName() {
        return viewExtensionButtonName;
    }

    public Integer getViewExtensionProductCountPerClick() {
        return viewExtensionProductCountPerClick;
    }

    public Integer getViewExtensionMaxClickCount() {
        return viewExtensionMaxClickCount;
    }

    public String getViewExtensionAfterMaxActionType() {
        return viewExtensionAfterMaxActionType;
    }

    public String getViewExtensionAfterMaxActionLinkUrl() {
        return viewExtensionAfterMaxActionLinkUrl;
    }

    public String getSpecData() {
        return specData;
    }
}

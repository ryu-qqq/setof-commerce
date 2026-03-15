package com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ComponentFixedProductJpaEntity - 컴포넌트 고정 상품 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "component_fixed_product")
public class ComponentFixedProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "component_id", nullable = false)
    private long componentId;

    @Column(name = "tab_id")
    private Long tabId;

    @Column(name = "product_group_id", nullable = false)
    private long productGroupId;

    @Column(name = "display_name", length = 200)
    private String displayName;

    @Column(name = "display_image_url", length = 500)
    private String displayImageUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected ComponentFixedProductJpaEntity() {}

    private ComponentFixedProductJpaEntity(
            Long id,
            long componentId,
            Long tabId,
            long productGroupId,
            String displayName,
            String displayImageUrl,
            int displayOrder,
            Instant deletedAt) {
        this.id = id;
        this.componentId = componentId;
        this.tabId = tabId;
        this.productGroupId = productGroupId;
        this.displayName = displayName;
        this.displayImageUrl = displayImageUrl;
        this.displayOrder = displayOrder;
        this.deletedAt = deletedAt;
    }

    public static ComponentFixedProductJpaEntity create(
            Long id,
            long componentId,
            Long tabId,
            long productGroupId,
            String displayName,
            String displayImageUrl,
            int displayOrder,
            Instant deletedAt) {
        return new ComponentFixedProductJpaEntity(
                id,
                componentId,
                tabId,
                productGroupId,
                displayName,
                displayImageUrl,
                displayOrder,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public Long getTabId() {
        return tabId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayImageUrl() {
        return displayImageUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}

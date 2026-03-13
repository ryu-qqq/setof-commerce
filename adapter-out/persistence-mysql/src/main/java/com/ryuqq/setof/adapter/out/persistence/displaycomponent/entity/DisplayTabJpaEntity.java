package com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "display_tab")
public class DisplayTabJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "component_id", nullable = false)
    private long componentId;

    @Column(name = "tab_name", nullable = false, length = 100)
    private String tabName;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected DisplayTabJpaEntity() {}

    private DisplayTabJpaEntity(
            Long id,
            long componentId,
            String tabName,
            int displayOrder,
            Instant createdAt,
            Instant deletedAt) {
        this.id = id;
        this.componentId = componentId;
        this.tabName = tabName;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public static DisplayTabJpaEntity create(
            Long id,
            long componentId,
            String tabName,
            int displayOrder,
            Instant createdAt,
            Instant deletedAt) {
        return new DisplayTabJpaEntity(
                id, componentId, tabName, displayOrder, createdAt, deletedAt);
    }

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public String getTabName() {
        return tabName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}

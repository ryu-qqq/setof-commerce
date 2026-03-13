package com.ryuqq.setof.adapter.out.persistence.navigation.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * NavigationMenuJpaEntity - 네비게이션 메뉴 JPA 엔티티.
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
@Table(name = "navigation_menu")
public class NavigationMenuJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "link_url", nullable = false, length = 500)
    private String linkUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "display_start_at", nullable = false)
    private Instant displayStartAt;

    @Column(name = "display_end_at", nullable = false)
    private Instant displayEndAt;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected NavigationMenuJpaEntity() {
        super();
    }

    private NavigationMenuJpaEntity(
            Long id,
            String title,
            String linkUrl,
            int displayOrder,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.title = title;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayStartAt = displayStartAt;
        this.displayEndAt = displayEndAt;
        this.active = active;
    }

    public static NavigationMenuJpaEntity create(
            Long id,
            String title,
            String linkUrl,
            int displayOrder,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new NavigationMenuJpaEntity(
                id,
                title,
                linkUrl,
                displayOrder,
                displayStartAt,
                displayEndAt,
                active,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
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
}

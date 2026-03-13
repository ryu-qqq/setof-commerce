package com.ryuqq.setof.adapter.out.persistence.banner.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * BannerGroupJpaEntity - 배너 그룹 JPA 엔티티.
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
@Table(name = "banner_group")
public class BannerGroupJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "banner_type", nullable = false, length = 50)
    private String bannerType;

    @Column(name = "display_start_at", nullable = false)
    private Instant displayStartAt;

    @Column(name = "display_end_at", nullable = false)
    private Instant displayEndAt;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected BannerGroupJpaEntity() {
        super();
    }

    private BannerGroupJpaEntity(
            Long id,
            String title,
            String bannerType,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.title = title;
        this.bannerType = bannerType;
        this.displayStartAt = displayStartAt;
        this.displayEndAt = displayEndAt;
        this.active = active;
    }

    public static BannerGroupJpaEntity create(
            Long id,
            String title,
            String bannerType,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new BannerGroupJpaEntity(
                id,
                title,
                bannerType,
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

    public String getBannerType() {
        return bannerType;
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

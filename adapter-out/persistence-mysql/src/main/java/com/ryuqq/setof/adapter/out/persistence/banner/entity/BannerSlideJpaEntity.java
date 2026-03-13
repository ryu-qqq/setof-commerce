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
 * BannerSlideJpaEntity - 배너 슬라이드 JPA 엔티티.
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
@Table(name = "banner_slide")
public class BannerSlideJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banner_group_id", nullable = false)
    private long bannerGroupId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

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

    protected BannerSlideJpaEntity() {
        super();
    }

    private BannerSlideJpaEntity(
            Long id,
            long bannerGroupId,
            String title,
            String imageUrl,
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
        this.bannerGroupId = bannerGroupId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayStartAt = displayStartAt;
        this.displayEndAt = displayEndAt;
        this.active = active;
    }

    public static BannerSlideJpaEntity create(
            Long id,
            long bannerGroupId,
            String title,
            String imageUrl,
            String linkUrl,
            int displayOrder,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new BannerSlideJpaEntity(
                id,
                bannerGroupId,
                title,
                imageUrl,
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

    public long getBannerGroupId() {
        return bannerGroupId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
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

package com.ryuqq.setof.adapter.out.persistence.contentpage.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "content_page")
public class ContentPageJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "memo", length = 500)
    private String memo;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "display_start_at", nullable = false)
    private Instant displayStartAt;

    @Column(name = "display_end_at", nullable = false)
    private Instant displayEndAt;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected ContentPageJpaEntity() {
        super();
    }

    private ContentPageJpaEntity(
            Long id,
            String title,
            String memo,
            String imageUrl,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.title = title;
        this.memo = memo;
        this.imageUrl = imageUrl;
        this.displayStartAt = displayStartAt;
        this.displayEndAt = displayEndAt;
        this.active = active;
    }

    public static ContentPageJpaEntity create(
            Long id,
            String title,
            String memo,
            String imageUrl,
            Instant displayStartAt,
            Instant displayEndAt,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ContentPageJpaEntity(
                id,
                title,
                memo,
                imageUrl,
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

    public String getMemo() {
        return memo;
    }

    public String getImageUrl() {
        return imageUrl;
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

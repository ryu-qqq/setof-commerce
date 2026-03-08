package com.ryuqq.setof.adapter.out.persistence.reviewimage.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ReviewImageJpaEntity - 리뷰 이미지 JPA 엔티티.
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
@Table(name = "review_image")
public class ReviewImageJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long id;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    protected ReviewImageJpaEntity() {
        super();
    }

    private ReviewImageJpaEntity(
            Long id,
            Long reviewId,
            String imageUrl,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    public static ReviewImageJpaEntity create(
            Long id,
            Long reviewId,
            String imageUrl,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ReviewImageJpaEntity(
                id, reviewId, imageUrl, displayOrder, createdAt, updatedAt, deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}

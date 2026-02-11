package com.ryuqq.setof.storage.legacy.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyReviewImageEntity - 레거시 리뷰 이미지 엔티티.
 *
 * <p>레거시 DB의 review_image 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "review_image")
public class LegacyReviewImageEntity {

    @Id
    @Column(name = "review_image_id")
    private Long id;

    @Column(name = "review_id")
    private long reviewId;

    @Column(name = "review_image_type")
    private String reviewImageType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyReviewImageEntity() {}

    public Long getId() {
        return id;
    }

    public long getReviewId() {
        return reviewId;
    }

    public String getReviewImageType() {
        return reviewImageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

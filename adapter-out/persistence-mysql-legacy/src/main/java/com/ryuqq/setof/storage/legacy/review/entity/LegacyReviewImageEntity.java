package com.ryuqq.setof.storage.legacy.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private LegacyReviewEntity.Yn deleteYn;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyReviewImageEntity() {}

    private LegacyReviewImageEntity(
            Long id,
            long reviewId,
            String reviewImageType,
            String imageUrl,
            LegacyReviewEntity.Yn deleteYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        this.id = id;
        this.reviewId = reviewId;
        this.reviewImageType = reviewImageType;
        this.imageUrl = imageUrl;
        this.deleteYn = deleteYn;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    /** 신규 리뷰 이미지 엔티티 생성. */
    public static LegacyReviewImageEntity create(
            long reviewId,
            String reviewImageType,
            String imageUrl,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new LegacyReviewImageEntity(
                null,
                reviewId,
                reviewImageType,
                imageUrl,
                LegacyReviewEntity.Yn.N,
                insertDate,
                updateDate);
    }

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

    public LegacyReviewEntity.Yn getDeleteYn() {
        return deleteYn;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

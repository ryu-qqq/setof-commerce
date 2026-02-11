package com.ryuqq.setof.storage.legacy.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyProductRatingStatsEntity - 레거시 상품 평점 통계 엔티티.
 *
 * <p>레거시 DB의 product_rating_stats 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_rating_stats")
public class LegacyProductRatingStatsEntity {

    @Id
    @Column(name = "product_group_id")
    private Long id;

    @Column(name = "average_rating")
    private double averageRating;

    @Column(name = "review_count")
    private long reviewCount;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyProductRatingStatsEntity() {}

    public Long getId() {
        return id;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

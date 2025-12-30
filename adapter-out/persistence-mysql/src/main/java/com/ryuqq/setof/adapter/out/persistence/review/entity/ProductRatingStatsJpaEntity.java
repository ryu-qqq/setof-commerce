package com.ryuqq.setof.adapter.out.persistence.review.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * ProductRatingStatsJpaEntity - 상품 평점 통계 JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_rating_stats 테이블과 매핑됩니다.
 *
 * <p><strong>통계 규칙:</strong>
 *
 * <ul>
 *   <li>상품 그룹당 1개의 통계 레코드
 *   <li>평균 평점: 소수점 2자리 (HALF_UP)
 *   <li>리뷰 수: 0 이상
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>productGroupId: 상품 그룹 FK (Long)
 *   <li>JPA 관계 어노테이션 금지
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "product_rating_stats")
public class ProductRatingStatsJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품 그룹 ID (Long FK, Unique) */
    @Column(name = "product_group_id", nullable = false, unique = true)
    private Long productGroupId;

    /** 평균 평점 (소수점 2자리) */
    @Column(name = "average_rating", nullable = false, precision = 3, scale = 2)
    private BigDecimal averageRating;

    /** 리뷰 총 개수 */
    @Column(name = "review_count", nullable = false)
    private long reviewCount;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected ProductRatingStatsJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private ProductRatingStatsJpaEntity(
            Long id,
            Long productGroupId,
            BigDecimal averageRating,
            long reviewCount,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id 통계 ID (null이면 신규 생성)
     * @param productGroupId 상품 그룹 ID
     * @param averageRating 평균 평점
     * @param reviewCount 리뷰 개수
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @return ProductRatingStatsJpaEntity 인스턴스
     */
    public static ProductRatingStatsJpaEntity of(
            Long id,
            Long productGroupId,
            BigDecimal averageRating,
            long reviewCount,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductRatingStatsJpaEntity(
                id, productGroupId, averageRating, reviewCount, createdAt, updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }
}

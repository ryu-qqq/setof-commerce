package com.ryuqq.setof.adapter.out.persistence.review.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ReviewImageJpaEntity - 리뷰 이미지 JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 review_images 테이블과 매핑됩니다.
 *
 * <p><strong>이미지 첨부 규칙:</strong>
 *
 * <ul>
 *   <li>리뷰당 최대 3장 제한
 *   <li>displayOrder로 순서 관리
 *   <li>PHOTO 타입만 지원 (MVP)
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>reviewId: 리뷰 외래키 (Long)
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
@Table(name = "review_images")
public class ReviewImageJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 리뷰 ID (Long FK) */
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    /** 이미지 URL */
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    /** 이미지 타입 (PHOTO) */
    @Column(name = "image_type", nullable = false, length = 20)
    private String imageType;

    /** 표시 순서 (0부터 시작) */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected ReviewImageJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private ReviewImageJpaEntity(
            Long id,
            Long reviewId,
            String imageUrl,
            String imageType,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.displayOrder = displayOrder;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id 이미지 ID (null이면 신규 생성)
     * @param reviewId 리뷰 ID
     * @param imageUrl 이미지 URL
     * @param imageType 이미지 타입
     * @param displayOrder 표시 순서
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @return ReviewImageJpaEntity 인스턴스
     */
    public static ReviewImageJpaEntity of(
            Long id,
            Long reviewId,
            String imageUrl,
            String imageType,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        return new ReviewImageJpaEntity(
                id, reviewId, imageUrl, imageType, displayOrder, createdAt, updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}

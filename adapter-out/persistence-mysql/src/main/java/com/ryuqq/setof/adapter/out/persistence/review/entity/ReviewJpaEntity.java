package com.ryuqq.setof.adapter.out.persistence.review.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ReviewJpaEntity - 리뷰 JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 reviews 테이블과 매핑됩니다.
 *
 * <p><strong>리뷰 규칙:</strong>
 *
 * <ul>
 *   <li>주문당 상품별 1개 리뷰만 작성 가능
 *   <li>평점 1~5점
 *   <li>이미지 최대 3장 (ReviewImageJpaEntity로 관리)
 * </ul>
 *
 * <p><strong>FK 전략:</strong>
 *
 * <ul>
 *   <li>memberId: 회원 FK (String UUID)
 *   <li>orderId: 주문 FK (Long)
 *   <li>productGroupId: 상품 그룹 FK (Long)
 *   <li>JPA 관계 어노테이션 금지
 * </ul>
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>Soft Delete 지원
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "reviews")
public class ReviewJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 회원 ID (UUID String) */
    @Column(name = "member_id", length = 36, nullable = false)
    private String memberId;

    /** 주문 ID (Long FK) */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /** 상품 그룹 ID (Long FK) */
    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    /** 평점 (1~5) */
    @Column(name = "rating", nullable = false)
    private int rating;

    /** 리뷰 내용 */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected ReviewJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private ReviewJpaEntity(
            Long id,
            String memberId,
            Long orderId,
            Long productGroupId,
            int rating,
            String content,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.productGroupId = productGroupId;
        this.rating = rating;
        this.content = content;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id 리뷰 ID (null이면 신규 생성)
     * @param memberId 회원 ID (UUID String)
     * @param orderId 주문 ID
     * @param productGroupId 상품 그룹 ID
     * @param rating 평점
     * @param content 리뷰 내용
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @param deletedAt 삭제 일시
     * @return ReviewJpaEntity 인스턴스
     */
    public static ReviewJpaEntity of(
            Long id,
            String memberId,
            Long orderId,
            Long productGroupId,
            int rating,
            String content,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ReviewJpaEntity(
                id,
                memberId,
                orderId,
                productGroupId,
                rating,
                content,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public int getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }
}

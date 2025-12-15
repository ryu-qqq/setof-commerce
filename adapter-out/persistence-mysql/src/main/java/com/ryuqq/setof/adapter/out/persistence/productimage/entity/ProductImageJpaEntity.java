package com.ryuqq.setof.adapter.out.persistence.productimage.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductImageJpaEntity - ProductImage JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_images 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 *   <li>이미지는 Soft Delete 미적용
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>productGroupId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "product_images")
public class ProductImageJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품그룹 ID (Long FK) */
    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    /** 이미지 타입 (MAIN, SUB, DETAIL) */
    @Column(name = "image_type", nullable = false, length = 20)
    private String imageType;

    /** 원본 URL */
    @Column(name = "origin_url", nullable = false, length = 500)
    private String originUrl;

    /** CDN URL */
    @Column(name = "cdn_url", nullable = false, length = 500)
    private String cdnUrl;

    /** 표시 순서 */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /** JPA 기본 생성자 (protected) */
    protected ProductImageJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductImageJpaEntity(
            Long id,
            Long productGroupId,
            String imageType,
            String originUrl,
            String cdnUrl,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.imageType = imageType;
        this.originUrl = originUrl;
        this.cdnUrl = cdnUrl;
        this.displayOrder = displayOrder;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductImageJpaEntity of(
            Long id,
            Long productGroupId,
            String imageType,
            String originUrl,
            String cdnUrl,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductImageJpaEntity(
                id,
                productGroupId,
                imageType,
                originUrl,
                cdnUrl,
                displayOrder,
                createdAt,
                updatedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getImageType() {
        return imageType;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}

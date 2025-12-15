package com.ryuqq.setof.adapter.out.persistence.productdescription.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductDescriptionImageJpaEntity - ProductDescription Image JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_description_images 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>productDescriptionId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "product_description_images")
public class ProductDescriptionImageJpaEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품설명 ID (Long FK) */
    @Column(name = "product_description_id", nullable = false)
    private Long productDescriptionId;

    /** 표시 순서 */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /** 원본 URL */
    @Column(name = "origin_url", nullable = false, length = 1000)
    private String originUrl;

    /** CDN URL */
    @Column(name = "cdn_url", nullable = false, length = 1000)
    private String cdnUrl;

    /** 업로드 일시 */
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    /** JPA 기본 생성자 (protected) */
    protected ProductDescriptionImageJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductDescriptionImageJpaEntity(
            Long id,
            Long productDescriptionId,
            Integer displayOrder,
            String originUrl,
            String cdnUrl,
            Instant uploadedAt) {
        this.id = id;
        this.productDescriptionId = productDescriptionId;
        this.displayOrder = displayOrder;
        this.originUrl = originUrl;
        this.cdnUrl = cdnUrl;
        this.uploadedAt = uploadedAt;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductDescriptionImageJpaEntity of(
            Long id,
            Long productDescriptionId,
            Integer displayOrder,
            String originUrl,
            String cdnUrl,
            Instant uploadedAt) {
        return new ProductDescriptionImageJpaEntity(
                id, productDescriptionId, displayOrder, originUrl, cdnUrl, uploadedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getProductDescriptionId() {
        return productDescriptionId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }
}

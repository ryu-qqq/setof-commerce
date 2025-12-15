package com.ryuqq.setof.adapter.out.persistence.productdescription.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductDescriptionJpaEntity - ProductDescription JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_descriptions 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
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
@Table(name = "product_descriptions")
public class ProductDescriptionJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품그룹 ID (Long FK, UNIQUE) */
    @Column(name = "product_group_id", nullable = false, unique = true)
    private Long productGroupId;

    /** HTML 컨텐츠 */
    @Lob
    @Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    /** JPA 기본 생성자 (protected) */
    protected ProductDescriptionJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductDescriptionJpaEntity(
            Long id,
            Long productGroupId,
            String htmlContent,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.htmlContent = htmlContent;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductDescriptionJpaEntity of(
            Long id,
            Long productGroupId,
            String htmlContent,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductDescriptionJpaEntity(
                id, productGroupId, htmlContent, createdAt, updatedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getHtmlContent() {
        return htmlContent;
    }
}

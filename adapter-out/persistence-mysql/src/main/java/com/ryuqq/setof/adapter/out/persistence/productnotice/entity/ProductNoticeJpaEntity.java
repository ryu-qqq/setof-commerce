package com.ryuqq.setof.adapter.out.persistence.productnotice.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductNoticeJpaEntity - ProductNotice JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_notices 테이블과 매핑됩니다.
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
 *   <li>templateId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "product_notices")
public class ProductNoticeJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품그룹 ID (Long FK, UNIQUE) */
    @Column(name = "product_group_id", nullable = false, unique = true)
    private Long productGroupId;

    /** 템플릿 ID (Long FK) */
    @Column(name = "template_id", nullable = false)
    private Long templateId;

    /** JPA 기본 생성자 (protected) */
    protected ProductNoticeJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductNoticeJpaEntity(
            Long id, Long productGroupId, Long templateId, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.templateId = templateId;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductNoticeJpaEntity of(
            Long id, Long productGroupId, Long templateId, Instant createdAt, Instant updatedAt) {
        return new ProductNoticeJpaEntity(id, productGroupId, templateId, createdAt, updatedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public Long getTemplateId() {
        return templateId;
    }
}

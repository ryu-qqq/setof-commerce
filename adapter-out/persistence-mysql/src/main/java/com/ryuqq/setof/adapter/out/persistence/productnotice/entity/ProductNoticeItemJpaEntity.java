package com.ryuqq.setof.adapter.out.persistence.productnotice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ProductNoticeItemJpaEntity - ProductNotice Item JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 product_notice_items 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>productNoticeId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "product_notice_items")
public class ProductNoticeItemJpaEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 상품고시 ID (Long FK) */
    @Column(name = "product_notice_id", nullable = false)
    private Long productNoticeId;

    /** 필드 키 */
    @Column(name = "field_key", nullable = false, length = 50)
    private String fieldKey;

    /** 필드 값 */
    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue;

    /** 표시 순서 */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /** JPA 기본 생성자 (protected) */
    protected ProductNoticeItemJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ProductNoticeItemJpaEntity(
            Long id,
            Long productNoticeId,
            String fieldKey,
            String fieldValue,
            Integer displayOrder) {
        this.id = id;
        this.productNoticeId = productNoticeId;
        this.fieldKey = fieldKey;
        this.fieldValue = fieldValue;
        this.displayOrder = displayOrder;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ProductNoticeItemJpaEntity of(
            Long id,
            Long productNoticeId,
            String fieldKey,
            String fieldValue,
            Integer displayOrder) {
        return new ProductNoticeItemJpaEntity(
                id, productNoticeId, fieldKey, fieldValue, displayOrder);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getProductNoticeId() {
        return productNoticeId;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}

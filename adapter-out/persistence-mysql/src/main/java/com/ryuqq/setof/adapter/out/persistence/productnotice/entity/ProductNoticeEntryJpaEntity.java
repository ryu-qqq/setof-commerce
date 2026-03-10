package com.ryuqq.setof.adapter.out.persistence.productnotice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ProductNoticeEntryJpaEntity - 상품고시 항목 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 */
@Entity
@Table(name = "product_notice_entries")
public class ProductNoticeEntryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_notice_id", nullable = false)
    private Long productNoticeId;

    @Column(name = "notice_field_id", nullable = false)
    private Long noticeFieldId;

    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;

    @Column(name = "field_value", length = 500)
    private String fieldValue;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected ProductNoticeEntryJpaEntity() {}

    private ProductNoticeEntryJpaEntity(
            Long id,
            Long productNoticeId,
            Long noticeFieldId,
            String fieldName,
            String fieldValue,
            int sortOrder) {
        this.id = id;
        this.productNoticeId = productNoticeId;
        this.noticeFieldId = noticeFieldId;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.sortOrder = sortOrder;
    }

    public static ProductNoticeEntryJpaEntity create(
            Long id,
            Long productNoticeId,
            Long noticeFieldId,
            String fieldName,
            String fieldValue,
            int sortOrder) {
        return new ProductNoticeEntryJpaEntity(
                id, productNoticeId, noticeFieldId, fieldName, fieldValue, sortOrder);
    }

    public Long getId() {
        return id;
    }

    public Long getProductNoticeId() {
        return productNoticeId;
    }

    public Long getNoticeFieldId() {
        return noticeFieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}

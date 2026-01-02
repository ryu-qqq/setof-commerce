package com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * NoticeTemplateFieldJpaEntity - NoticeTemplateField JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 notice_template_fields 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>templateId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "notice_template_fields")
public class NoticeTemplateFieldJpaEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 템플릿 ID (Long FK) */
    @Column(name = "template_id", nullable = false)
    private Long templateId;

    /** 필드 키 */
    @Column(name = "field_key", nullable = false, length = 50)
    private String fieldKey;

    /** 필드 설명 */
    @Column(name = "description", length = 200)
    private String description;

    /** 필수 여부 */
    @Column(name = "required", nullable = false)
    private boolean required;

    /** 표시 순서 */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /** JPA 기본 생성자 (protected) */
    protected NoticeTemplateFieldJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private NoticeTemplateFieldJpaEntity(
            Long id,
            Long templateId,
            String fieldKey,
            String description,
            boolean required,
            int displayOrder) {
        this.id = id;
        this.templateId = templateId;
        this.fieldKey = fieldKey;
        this.description = description;
        this.required = required;
        this.displayOrder = displayOrder;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static NoticeTemplateFieldJpaEntity of(
            Long id,
            Long templateId,
            String fieldKey,
            String description,
            boolean required,
            int displayOrder) {
        return new NoticeTemplateFieldJpaEntity(
                id, templateId, fieldKey, description, required, displayOrder);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}

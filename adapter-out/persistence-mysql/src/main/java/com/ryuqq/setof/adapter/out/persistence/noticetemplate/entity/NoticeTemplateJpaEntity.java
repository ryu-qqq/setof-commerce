package com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * NoticeTemplateJpaEntity - NoticeTemplate JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 notice_templates 테이블과 매핑됩니다.
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
 *   <li>categoryId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "notice_templates")
public class NoticeTemplateJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 카테고리 ID (Long FK, UNIQUE) */
    @Column(name = "category_id", nullable = false, unique = true)
    private Long categoryId;

    /** 템플릿명 */
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    /** JPA 기본 생성자 (protected) */
    protected NoticeTemplateJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private NoticeTemplateJpaEntity(
            Long id, Long categoryId, String templateName, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.categoryId = categoryId;
        this.templateName = templateName;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static NoticeTemplateJpaEntity of(
            Long id, Long categoryId, String templateName, Instant createdAt, Instant updatedAt) {
        return new NoticeTemplateJpaEntity(id, categoryId, templateName, createdAt, updatedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getTemplateName() {
        return templateName;
    }
}

package com.ryuqq.setof.adapter.out.persistence.faqcategory.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * FaqCategoryJpaEntity - FAQ 카테고리 JPA Entity
 *
 * <p>cms_faq_categories 테이블과 매핑되는 JPA Entity입니다.
 *
 * <p>Persistence Layer 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Plain Java 사용
 *   <li>SoftDeletableEntity 상속 (createdAt, updatedAt, deletedAt)
 *   <li>Long FK 전략 - JPA 관계 어노테이션 금지
 *   <li>Static Factory Method 사용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_faq_categories")
public class FaqCategoryJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String description;
    private int displayOrder;
    private String status;

    /** JPA 전용 기본 생성자 */
    protected FaqCategoryJpaEntity() {}

    private FaqCategoryJpaEntity(
            Long id,
            String code,
            String name,
            String description,
            int displayOrder,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
        this.status = status;
    }

    /**
     * Static Factory Method
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param name 카테고리명
     * @param description 설명
     * @param displayOrder 표시 순서
     * @param status 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @return FaqCategoryJpaEntity 인스턴스
     */
    public static FaqCategoryJpaEntity of(
            Long id,
            String code,
            String name,
            String description,
            int displayOrder,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new FaqCategoryJpaEntity(
                id, code, name, description, displayOrder, status, createdAt, updatedAt, deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getStatus() {
        return status;
    }
}

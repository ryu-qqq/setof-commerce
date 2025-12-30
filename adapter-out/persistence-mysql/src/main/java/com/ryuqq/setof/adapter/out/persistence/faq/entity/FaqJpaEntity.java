package com.ryuqq.setof.adapter.out.persistence.faq.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * FaqJpaEntity - FAQ JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_faqs 테이블과 매핑됩니다.
 *
 * <p>Lombok 금지 - Plain Java 사용
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_faqs")
public class FaqJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 카테고리 코드 */
    @Column(name = "category_code", nullable = false, length = 50)
    private String categoryCode;

    /** 질문 */
    @Column(name = "question", nullable = false, length = 500)
    private String question;

    /** 답변 */
    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    /** 상단 노출 여부 */
    @Column(name = "is_top", nullable = false)
    private boolean top;

    /** 상단 순서 */
    @Column(name = "top_order", nullable = false)
    private int topOrder;

    /** 표시 순서 */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /** 상태 (DRAFT, PUBLISHED, HIDDEN) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 조회수 */
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    /** JPA 기본 생성자 (protected) */
    protected FaqJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private FaqJpaEntity(
            Long id,
            String categoryCode,
            String question,
            String answer,
            boolean top,
            int topOrder,
            int displayOrder,
            String status,
            int viewCount,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.categoryCode = categoryCode;
        this.question = question;
        this.answer = answer;
        this.top = top;
        this.topOrder = topOrder;
        this.displayOrder = displayOrder;
        this.status = status;
        this.viewCount = viewCount;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static FaqJpaEntity of(
            Long id,
            String categoryCode,
            String question,
            String answer,
            boolean top,
            int topOrder,
            int displayOrder,
            String status,
            int viewCount,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new FaqJpaEntity(
                id,
                categoryCode,
                question,
                answer,
                top,
                topOrder,
                displayOrder,
                status,
                viewCount,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isTop() {
        return top;
    }

    public int getTopOrder() {
        return topOrder;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getStatus() {
        return status;
    }

    public int getViewCount() {
        return viewCount;
    }
}

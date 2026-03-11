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
 * FaqJpaEntity - FAQ JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "faq")
public class FaqJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faq_type", nullable = false, length = 30)
    private String faqType;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "contents", nullable = false, length = 2000)
    private String contents;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "top_display_order")
    private Integer topDisplayOrder;

    protected FaqJpaEntity() {
        super();
    }

    private FaqJpaEntity(
            Long id,
            String faqType,
            String title,
            String contents,
            int displayOrder,
            Integer topDisplayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.faqType = faqType;
        this.title = title;
        this.contents = contents;
        this.displayOrder = displayOrder;
        this.topDisplayOrder = topDisplayOrder;
    }

    public static FaqJpaEntity create(
            Long id,
            String faqType,
            String title,
            String contents,
            int displayOrder,
            Integer topDisplayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new FaqJpaEntity(
                id,
                faqType,
                title,
                contents,
                displayOrder,
                topDisplayOrder,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public String getFaqType() {
        return faqType;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Integer getTopDisplayOrder() {
        return topDisplayOrder;
    }
}

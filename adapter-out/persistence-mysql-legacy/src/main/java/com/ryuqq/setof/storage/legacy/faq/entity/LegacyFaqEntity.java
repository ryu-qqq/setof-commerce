package com.ryuqq.setof.storage.legacy.faq.entity;

import com.ryuqq.setof.domain.legacy.faq.FaqType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyFaqEntity - 레거시 FAQ 엔티티.
 *
 * <p>레거시 DB의 faq 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "faq")
public class LegacyFaqEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "faq_type")
    private FaqType faqType;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "top_display_order")
    private Integer topDisplayOrder;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyFaqEntity() {}

    public Long getId() {
        return id;
    }

    public FaqType getFaqType() {
        return faqType;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Integer getTopDisplayOrder() {
        return topDisplayOrder;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

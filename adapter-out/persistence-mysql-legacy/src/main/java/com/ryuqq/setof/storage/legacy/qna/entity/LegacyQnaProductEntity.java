package com.ryuqq.setof.storage.legacy.qna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyQnaProductEntity - 레거시 Q&A-상품 매핑 엔티티.
 *
 * <p>레거시 DB의 qna_product 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "qna_product")
public class LegacyQnaProductEntity {

    @Id
    @Column(name = "qna_product_id")
    private Long id;

    @Column(name = "qna_id")
    private Long qnaId;

    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyQnaProductEntity() {}

    public Long getId() {
        return id;
    }

    public Long getQnaId() {
        return qnaId;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

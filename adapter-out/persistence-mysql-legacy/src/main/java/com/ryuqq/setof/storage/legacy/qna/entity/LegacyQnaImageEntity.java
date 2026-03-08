package com.ryuqq.setof.storage.legacy.qna.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
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
 * LegacyQnaImageEntity - 레거시 Q&A 이미지 엔티티.
 *
 * <p>레거시 DB의 qna_image 테이블 매핑. ORDER Q&A 전용.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "qna_image")
public class LegacyQnaImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_image_id")
    private Long id;

    @Column(name = "qna_id")
    private Long qnaId;

    @Column(name = "qna_answer_id")
    private Long qnaAnswerId;

    @Column(name = "qna_issue_type")
    private String qnaIssueType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "display_order")
    private Long displayOrder;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "insert_operator")
    private String insertOperator;

    @Column(name = "update_operator")
    private String updateOperator;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyQnaImageEntity() {}

    public static LegacyQnaImageEntity create(
            Long qnaId,
            Long qnaAnswerId,
            String qnaIssueType,
            String imageUrl,
            Long displayOrder,
            Yn deleteYn,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyQnaImageEntity entity = new LegacyQnaImageEntity();
        entity.qnaId = qnaId;
        entity.qnaAnswerId = qnaAnswerId;
        entity.qnaIssueType = qnaIssueType;
        entity.imageUrl = imageUrl;
        entity.displayOrder = displayOrder;
        entity.deleteYn = deleteYn;
        entity.insertOperator = insertOperator;
        entity.updateOperator = updateOperator;
        entity.insertDate = insertDate;
        entity.updateDate = updateDate;
        return entity;
    }

    public Long getId() {
        return id;
    }

    public Long getQnaId() {
        return qnaId;
    }

    public Long getQnaAnswerId() {
        return qnaAnswerId;
    }

    public String getQnaIssueType() {
        return qnaIssueType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public String getInsertOperator() {
        return insertOperator;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

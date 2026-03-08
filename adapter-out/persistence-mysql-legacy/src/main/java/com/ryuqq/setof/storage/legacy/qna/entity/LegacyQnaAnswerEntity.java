package com.ryuqq.setof.storage.legacy.qna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyQnaAnswerEntity - 레거시 Q&A 답변 엔티티.
 *
 * <p>레거시 DB의 qna_answer 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "qna_answer")
public class LegacyQnaAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_answer_id")
    private Long id;

    @Column(name = "qna_id")
    private Long qnaId;

    @Column(name = "qna_parent_id")
    private Long qnaParentId;

    @Column(name = "qna_writer_type")
    private String qnaWriterType;

    @Column(name = "qna_status")
    private String qnaStatus;

    @Embedded private LegacyQnaContents qnaContents;

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

    protected LegacyQnaAnswerEntity() {}

    public static LegacyQnaAnswerEntity create(
            Long qnaId,
            Long qnaParentId,
            String qnaWriterType,
            String qnaStatus,
            LegacyQnaContents qnaContents,
            Yn deleteYn,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyQnaAnswerEntity entity = new LegacyQnaAnswerEntity();
        entity.qnaId = qnaId;
        entity.qnaParentId = qnaParentId;
        entity.qnaWriterType = qnaWriterType;
        entity.qnaStatus = qnaStatus;
        entity.qnaContents = qnaContents;
        entity.deleteYn = deleteYn;
        entity.insertOperator = insertOperator;
        entity.updateOperator = updateOperator;
        entity.insertDate = insertDate;
        entity.updateDate = updateDate;
        return entity;
    }

    public static LegacyQnaAnswerEntity reconstitute(
            Long id,
            Long qnaId,
            Long qnaParentId,
            String qnaWriterType,
            String qnaStatus,
            LegacyQnaContents qnaContents,
            Yn deleteYn,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyQnaAnswerEntity entity =
                create(
                        qnaId,
                        qnaParentId,
                        qnaWriterType,
                        qnaStatus,
                        qnaContents,
                        deleteYn,
                        insertOperator,
                        updateOperator,
                        insertDate,
                        updateDate);
        entity.id = id;
        return entity;
    }

    public Long getId() {
        return id;
    }

    public Long getQnaId() {
        return qnaId;
    }

    public Long getQnaParentId() {
        return qnaParentId;
    }

    public String getQnaWriterType() {
        return qnaWriterType;
    }

    public String getQnaStatus() {
        return qnaStatus;
    }

    public LegacyQnaContents getQnaContents() {
        return qnaContents;
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

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}

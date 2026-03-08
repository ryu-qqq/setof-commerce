package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.qna.id.LegacyQnaAnswerId;
import com.ryuqq.setof.domain.qna.id.QnaAnswerId;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import java.time.Instant;

/**
 * QnaAnswer - Q&A 답변 엔티티.
 *
 * <p>Qna Aggregate의 하위 엔티티로, 판매자가 작성한 답변을 나타냅니다.
 *
 * <p>DOM-CMN-001: 순수 자바 객체 (POJO).
 *
 * <p>DOM-AGG-004: Setter 금지. 비즈니스 메서드(editContent, delete)로만 변경.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaAnswer {

    private final QnaAnswerId id;
    private final LegacyQnaAnswerId legacyId;
    private QnaContent content;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private QnaAnswer(
            QnaAnswerId id,
            LegacyQnaAnswerId legacyId,
            QnaContent content,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.legacyId = legacyId;
        this.content = content;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 신규 답변 생성. */
    public static QnaAnswer create(QnaContent content, Instant occurredAt) {
        return new QnaAnswer(
                QnaAnswerId.forNew(),
                LegacyQnaAnswerId.forNew(),
                content,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static QnaAnswer reconstitute(
            QnaAnswerId id,
            LegacyQnaAnswerId legacyId,
            QnaContent content,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new QnaAnswer(id, legacyId, content, deletionStatus, createdAt, updatedAt);
    }

    /**
     * 답변 내용 수정.
     *
     * @param newContent 새 내용
     * @param occurredAt 수정 시각
     */
    public void editContent(QnaContent newContent, Instant occurredAt) {
        this.content = newContent;
        this.updatedAt = occurredAt;
    }

    /**
     * 답변 소프트 삭제.
     *
     * @param occurredAt 삭제 시각
     */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public QnaAnswerId id() {
        return id;
    }

    public String idValue() {
        return id.value();
    }

    public LegacyQnaAnswerId legacyId() {
        return legacyId;
    }

    public Long legacyIdValue() {
        return legacyId != null ? legacyId.value() : null;
    }

    public QnaContent content() {
        return content;
    }

    public String contentValue() {
        return content.value();
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}

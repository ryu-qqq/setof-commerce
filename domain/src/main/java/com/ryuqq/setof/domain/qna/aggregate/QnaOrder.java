package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import java.time.Instant;

/**
 * QnaOrder - Q&A 주문 매핑 Aggregate.
 *
 * <p>Q&A와 주문 간의 매핑을 관리합니다. 레거시 DB의 qna_order 테이블에 대응합니다.
 *
 * <p>forNew() 시점에는 qnaId가 없으며, Facade에서 Qna persist 후 withQnaId()로 세팅합니다.
 *
 * <p>DOM-AGG-001: static 팩토리 메서드 사용.
 *
 * <p>DOM-AGG-004: Setter 금지.
 *
 * <p>DOM-CMN-001: 순수 자바 객체 (POJO).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaOrder {

    private final LegacyQnaId qnaId;
    private final LegacyOrderId legacyOrderId;
    private final Instant createdAt;

    private QnaOrder(LegacyQnaId qnaId, LegacyOrderId legacyOrderId, Instant createdAt) {
        this.qnaId = qnaId;
        this.legacyOrderId = legacyOrderId;
        this.createdAt = createdAt;
    }

    public static QnaOrder forNew(LegacyOrderId legacyOrderId, Instant occurredAt) {
        if (legacyOrderId == null) {
            throw new IllegalArgumentException("QnaOrder의 legacyOrderId는 null일 수 없습니다");
        }
        return new QnaOrder(null, legacyOrderId, occurredAt);
    }

    public static QnaOrder reconstitute(
            LegacyQnaId qnaId, LegacyOrderId legacyOrderId, Instant createdAt) {
        return new QnaOrder(qnaId, legacyOrderId, createdAt);
    }

    /**
     * Qna persist 후 반환된 qnaId를 세팅한 새 인스턴스 반환.
     *
     * @param qnaId 저장된 레거시 Q&A ID
     * @return qnaId가 세팅된 QnaOrder
     */
    public QnaOrder withQnaId(LegacyQnaId qnaId) {
        return new QnaOrder(qnaId, this.legacyOrderId, this.createdAt);
    }

    public LegacyQnaId qnaId() {
        return qnaId;
    }

    public Long qnaIdValue() {
        return qnaId != null ? qnaId.value() : null;
    }

    public LegacyOrderId legacyOrderId() {
        return legacyOrderId;
    }

    public Long legacyOrderIdValue() {
        return legacyOrderId != null ? legacyOrderId.value() : null;
    }

    public Instant createdAt() {
        return createdAt;
    }
}

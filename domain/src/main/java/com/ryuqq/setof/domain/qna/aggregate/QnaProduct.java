package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import java.time.Instant;

/**
 * QnaProduct - Q&A 상품 매핑 Aggregate.
 *
 * <p>Q&A와 상품그룹 간의 매핑을 관리합니다. 레거시 DB의 qna_product 테이블에 대응합니다.
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
public class QnaProduct {

    private final LegacyQnaId qnaId;
    private final ProductGroupId productGroupId;
    private final Instant createdAt;

    private QnaProduct(LegacyQnaId qnaId, ProductGroupId productGroupId, Instant createdAt) {
        this.qnaId = qnaId;
        this.productGroupId = productGroupId;
        this.createdAt = createdAt;
    }

    public static QnaProduct forNew(ProductGroupId productGroupId, Instant occurredAt) {
        if (productGroupId == null) {
            throw new IllegalArgumentException("QnaProduct의 productGroupId는 null일 수 없습니다");
        }
        return new QnaProduct(null, productGroupId, occurredAt);
    }

    public static QnaProduct reconstitute(
            LegacyQnaId qnaId, ProductGroupId productGroupId, Instant createdAt) {
        return new QnaProduct(qnaId, productGroupId, createdAt);
    }

    /**
     * Qna persist 후 반환된 qnaId를 세팅한 새 인스턴스 반환.
     *
     * @param qnaId 저장된 레거시 Q&A ID
     * @return qnaId가 세팅된 QnaProduct
     */
    public QnaProduct withQnaId(LegacyQnaId qnaId) {
        return new QnaProduct(qnaId, this.productGroupId, this.createdAt);
    }

    public LegacyQnaId qnaId() {
        return qnaId;
    }

    public Long qnaIdValue() {
        return qnaId != null ? qnaId.value() : null;
    }

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public Long productGroupIdValue() {
        return productGroupId != null ? productGroupId.value() : null;
    }

    public Instant createdAt() {
        return createdAt;
    }
}

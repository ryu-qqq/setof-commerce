package com.ryuqq.setof.application.qna.dto.bundle;

import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;

/**
 * ProductQnaBundle - 상품 Q&A 등록 번들.
 *
 * <p>Qna + QnaProduct를 하나의 단위로 묶어 Facade에 전달합니다.
 *
 * @param qna Q&A Aggregate
 * @param qnaProduct Q&A 상품 매핑 Aggregate
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductQnaBundle(Qna qna, QnaProduct qnaProduct) {

    public ProductQnaBundle {
        if (qna == null) {
            throw new IllegalArgumentException("ProductQnaBundle의 qna는 null일 수 없습니다");
        }
        if (qnaProduct == null) {
            throw new IllegalArgumentException("ProductQnaBundle의 qnaProduct는 null일 수 없습니다");
        }
    }
}

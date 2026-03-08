package com.ryuqq.setof.application.qna.dto.bundle;

import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;

/**
 * OrderQnaBundle - 주문 Q&A 등록 번들.
 *
 * <p>Qna + QnaOrder + QnaImages를 하나의 단위로 묶어 Facade에 전달합니다.
 *
 * @param qna Q&A Aggregate
 * @param qnaOrder Q&A 주문 매핑 Aggregate
 * @param images Q&A 이미지 컬렉션 (없으면 empty)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record OrderQnaBundle(Qna qna, QnaOrder qnaOrder, QnaImages images) {

    public OrderQnaBundle {
        if (qna == null) {
            throw new IllegalArgumentException("OrderQnaBundle의 qna는 null일 수 없습니다");
        }
        if (qnaOrder == null) {
            throw new IllegalArgumentException("OrderQnaBundle의 qnaOrder는 null일 수 없습니다");
        }
        if (images == null) {
            images = QnaImages.empty();
        }
    }
}

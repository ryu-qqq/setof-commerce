package com.ryuqq.setof.application.qna.port.out.command;

import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;

/**
 * QnaOrderCommandPort - Q&A 주문 매핑 명령 출력 포트.
 *
 * <p>Adapter(Persistence Layer)가 구현할 인터페이스입니다.
 * 레거시 DB의 qna_order 테이블에 주문 매핑을 영속합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaOrderCommandPort {

    /**
     * Q&A 주문 매핑 저장.
     *
     * @param qnaOrder Q&A 주문 매핑 Aggregate (qnaId 포함)
     */
    void persist(QnaOrder qnaOrder);
}

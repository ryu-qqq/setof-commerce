package com.ryuqq.setof.application.qna.port.out.command;

import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;

/**
 * QnaProductCommandPort - Q&A 상품 매핑 명령 출력 포트.
 *
 * <p>Adapter(Persistence Layer)가 구현할 인터페이스입니다.
 * 레거시 DB의 qna_product 테이블에 상품 매핑을 영속합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaProductCommandPort {

    /**
     * Q&A 상품 매핑 저장.
     *
     * @param qnaProduct Q&A 상품 매핑 Aggregate (qnaId 포함)
     */
    void persist(QnaProduct qnaProduct);
}

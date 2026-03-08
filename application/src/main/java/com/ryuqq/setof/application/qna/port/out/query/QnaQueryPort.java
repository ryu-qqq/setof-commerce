package com.ryuqq.setof.application.qna.port.out.query;

import com.ryuqq.setof.domain.qna.aggregate.Qna;
import java.util.Optional;

/**
 * QnaQueryPort - Q&A 단건 조회 출력 포트.
 *
 * <p>qna 테이블에서 ID 기반 단건 조회하여 Qna 도메인 객체를 반환합니다. 상품/주문 타입 구분 없이 범용 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaQueryPort {

    /**
     * Q&A ID로 단건 조회.
     *
     * @param qnaId 레거시 Q&A ID
     * @return Qna 도메인 객체
     */
    Optional<Qna> findById(long qnaId);
}

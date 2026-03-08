package com.ryuqq.setof.application.qna.port.out.command;

import com.ryuqq.setof.domain.qna.aggregate.Qna;

/**
 * QnaCommandPort - Q&A 명령 출력 포트.
 *
 * <p>Adapter(Persistence Layer)가 구현할 인터페이스입니다.
 * 레거시 DB에 Q&A 데이터를 영속합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaCommandPort {

    /**
     * Q&A 저장 (신규 등록 및 수정).
     *
     * <p>신규 Q&A는 INSERT, 기존 Q&A는 Dirty Checking으로 UPDATE 처리.
     *
     * @param qna Q&A Aggregate
     * @return 저장된 레거시 Q&A ID
     */
    Long persist(Qna qna);
}

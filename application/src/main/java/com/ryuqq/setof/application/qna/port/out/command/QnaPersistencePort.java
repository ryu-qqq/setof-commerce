package com.ryuqq.setof.application.qna.port.out.command;

import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaId;

/**
 * QnA Persistence Port (Command)
 *
 * <p>QnA Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface QnaPersistencePort {

    /**
     * QnA 저장 (신규 생성 또는 수정)
     *
     * @param qna 저장할 QnA (Domain Aggregate)
     * @return 저장된 QnA의 ID
     */
    QnaId persist(Qna qna);
}

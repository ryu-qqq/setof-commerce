package com.ryuqq.setof.application.qna.port.out.command;

import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;

/**
 * QnA Reply Persistence Port (Command)
 *
 * <p>QnA Reply를 영속화하는 쓰기 전용 Port
 *
 * <p><strong>규칙:</strong> Command Port는 persist() 단일 메서드로 저장/수정을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface QnaReplyPersistencePort {

    /**
     * QnA Reply 영속화 (저장/수정)
     *
     * <p>ID가 없으면 신규 저장 (Path 생성 포함), ID가 있으면 수정합니다 (upsert 패턴).
     *
     * @param reply 영속화할 QnA Reply (Domain Entity)
     * @return 저장된 Reply의 ID
     */
    QnaReplyId persist(QnaReply reply);
}

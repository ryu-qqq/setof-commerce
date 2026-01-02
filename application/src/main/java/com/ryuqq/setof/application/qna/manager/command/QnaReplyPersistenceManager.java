package com.ryuqq.setof.application.qna.manager.command;

import com.ryuqq.setof.application.qna.port.out.command.QnaReplyPersistencePort;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * QnA Reply Persistence Manager
 *
 * <p>QnA Reply 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * <p><strong>규칙:</strong> Manager는 persist() 단일 메서드로 저장/수정을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaReplyPersistenceManager {

    private final QnaReplyPersistencePort qnaReplyPersistencePort;

    public QnaReplyPersistenceManager(QnaReplyPersistencePort qnaReplyPersistencePort) {
        this.qnaReplyPersistencePort = qnaReplyPersistencePort;
    }

    /**
     * QnA Reply 영속화 (저장/수정)
     *
     * <p>ID가 없으면 신규 저장 (Path 생성 포함), ID가 있으면 수정합니다 (upsert 패턴).
     *
     * @param reply 영속화할 QnA Reply
     * @return 저장된 Reply의 ID
     */
    @Transactional
    public QnaReplyId persist(QnaReply reply) {
        return qnaReplyPersistencePort.persist(reply);
    }
}

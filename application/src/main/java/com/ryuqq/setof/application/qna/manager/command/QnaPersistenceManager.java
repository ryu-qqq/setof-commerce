package com.ryuqq.setof.application.qna.manager.command;

import com.ryuqq.setof.application.qna.port.out.command.QnaPersistencePort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * QnA Persistence Manager
 *
 * <p>QnA 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaPersistenceManager {

    private final QnaPersistencePort qnaPersistencePort;

    public QnaPersistenceManager(QnaPersistencePort qnaPersistencePort) {
        this.qnaPersistencePort = qnaPersistencePort;
    }

    /**
     * QnA 저장
     *
     * @param qna 저장할 QnA
     * @return 저장된 QnA의 ID
     */
    @Transactional
    public QnaId persist(Qna qna) {
        return qnaPersistencePort.persist(qna);
    }
}

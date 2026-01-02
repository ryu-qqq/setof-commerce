package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.CloseQnaCommand;
import com.ryuqq.setof.application.qna.manager.command.QnaPersistenceManager;
import com.ryuqq.setof.application.qna.manager.query.QnaReadManager;
import com.ryuqq.setof.application.qna.port.in.command.CloseQnaUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import org.springframework.stereotype.Service;

/**
 * 문의 종료 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnA 조회 (없으면 예외)
 *   <li>QnA 상태를 CLOSED로 변경
 *   <li>변경된 QnA 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CloseQnaService implements CloseQnaUseCase {

    private final QnaReadManager qnaReadManager;
    private final QnaPersistenceManager qnaPersistenceManager;

    public CloseQnaService(
            QnaReadManager qnaReadManager, QnaPersistenceManager qnaPersistenceManager) {
        this.qnaReadManager = qnaReadManager;
        this.qnaPersistenceManager = qnaPersistenceManager;
    }

    @Override
    public void execute(CloseQnaCommand command) {
        Qna qna = qnaReadManager.findById(command.qnaId());
        qna.close();
        qnaPersistenceManager.persist(qna);
    }
}

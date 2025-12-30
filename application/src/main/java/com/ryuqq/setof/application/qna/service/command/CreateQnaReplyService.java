package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.CreateQnaReplyCommand;
import com.ryuqq.setof.application.qna.factory.command.QnaCommandFactory;
import com.ryuqq.setof.application.qna.manager.command.QnaReplyPersistenceManager;
import com.ryuqq.setof.application.qna.manager.query.QnaReadManager;
import com.ryuqq.setof.application.qna.manager.query.QnaReplyReadManager;
import com.ryuqq.setof.application.qna.port.in.command.CreateQnaReplyUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.exception.QnaAlreadyClosedException;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import org.springframework.stereotype.Service;

/**
 * 문의 답변 생성 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnA 존재 및 상태 검증 (OPEN 상태만 답변 가능)
 *   <li>대댓글인 경우 부모 답변 존재 확인 및 경로 조회
 *   <li>QnaCommandFactory로 QnaReply 도메인 생성
 *   <li>QnaReplyPersistenceManager로 저장 (ID/Path 자동 생성)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateQnaReplyService implements CreateQnaReplyUseCase {

    private final QnaCommandFactory qnaCommandFactory;
    private final QnaReplyPersistenceManager qnaReplyPersistenceManager;
    private final QnaReadManager qnaReadManager;
    private final QnaReplyReadManager qnaReplyReadManager;

    public CreateQnaReplyService(
            QnaCommandFactory qnaCommandFactory,
            QnaReplyPersistenceManager qnaReplyPersistenceManager,
            QnaReadManager qnaReadManager,
            QnaReplyReadManager qnaReplyReadManager) {
        this.qnaCommandFactory = qnaCommandFactory;
        this.qnaReplyPersistenceManager = qnaReplyPersistenceManager;
        this.qnaReadManager = qnaReadManager;
        this.qnaReplyReadManager = qnaReplyReadManager;
    }

    @Override
    public Long execute(CreateQnaReplyCommand command) {
        Qna qna = qnaReadManager.findById(command.qnaId());
        validateQnaStatus(qna);

        QnaReply reply = createReply(command);
        QnaReplyId savedId = qnaReplyPersistenceManager.persist(reply);
        return savedId.getValue();
    }

    private void validateQnaStatus(Qna qna) {
        if (qna.getStatus() == QnaStatus.CLOSED) {
            throw new QnaAlreadyClosedException(qna.getId().getValue());
        }
    }

    private QnaReply createReply(CreateQnaReplyCommand command) {
        if (isRootReply(command)) {
            return qnaCommandFactory.createRootReply(command);
        }
        QnaReply parentReply = qnaReplyReadManager.findById(command.parentReplyId());
        return qnaCommandFactory.createChildReply(command, parentReply.getPath());
    }

    private boolean isRootReply(CreateQnaReplyCommand command) {
        return command.parentReplyId() == null;
    }
}

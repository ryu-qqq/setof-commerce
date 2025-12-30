package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.UpdateQnaReplyCommand;
import com.ryuqq.setof.application.qna.manager.command.QnaReplyPersistenceManager;
import com.ryuqq.setof.application.qna.manager.query.QnaReadManager;
import com.ryuqq.setof.application.qna.manager.query.QnaReplyReadManager;
import com.ryuqq.setof.application.qna.port.in.command.UpdateQnaReplyUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.exception.QnaAlreadyClosedException;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.ReplyContent;
import org.springframework.stereotype.Service;

/**
 * 문의 답변 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnA 존재 및 상태 검증 (OPEN 상태만 수정 가능)
 *   <li>답변 존재 확인 및 조회
 *   <li>도메인 메서드로 내용 수정
 *   <li>QnaReplyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateQnaReplyService implements UpdateQnaReplyUseCase {

    private final QnaReadManager qnaReadManager;
    private final QnaReplyReadManager qnaReplyReadManager;
    private final QnaReplyPersistenceManager qnaReplyPersistenceManager;

    public UpdateQnaReplyService(
            QnaReadManager qnaReadManager,
            QnaReplyReadManager qnaReplyReadManager,
            QnaReplyPersistenceManager qnaReplyPersistenceManager) {
        this.qnaReadManager = qnaReadManager;
        this.qnaReplyReadManager = qnaReplyReadManager;
        this.qnaReplyPersistenceManager = qnaReplyPersistenceManager;
    }

    @Override
    public void execute(UpdateQnaReplyCommand command) {
        Qna qna = qnaReadManager.findById(command.qnaId());
        validateQnaStatus(qna);

        QnaReply reply = qnaReplyReadManager.findById(command.replyId());
        QnaReply updatedReply = reply.updateContent(ReplyContent.of(command.content()));

        qnaReplyPersistenceManager.persist(updatedReply);
    }

    private void validateQnaStatus(Qna qna) {
        if (qna.getStatus() == QnaStatus.CLOSED) {
            throw new QnaAlreadyClosedException(qna.getId().getValue());
        }
    }
}

package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.UpdateQnaCommand;
import com.ryuqq.setof.application.qna.manager.command.QnaPersistenceManager;
import com.ryuqq.setof.application.qna.manager.query.QnaReadManager;
import com.ryuqq.setof.application.qna.port.in.command.UpdateQnaUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import org.springframework.stereotype.Service;

/**
 * 문의 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnA 존재 확인 및 조회
 *   <li>도메인 메서드로 내용 수정 (OPEN 상태 검증 포함)
 *   <li>QnaPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateQnaService implements UpdateQnaUseCase {

    private final QnaReadManager qnaReadManager;
    private final QnaPersistenceManager qnaPersistenceManager;

    public UpdateQnaService(
            QnaReadManager qnaReadManager, QnaPersistenceManager qnaPersistenceManager) {
        this.qnaReadManager = qnaReadManager;
        this.qnaPersistenceManager = qnaPersistenceManager;
    }

    @Override
    public void execute(UpdateQnaCommand command) {
        Qna qna = qnaReadManager.findById(command.qnaId());

        QnaContent newContent = QnaContent.of(command.title(), command.content());
        Qna updatedQna = qna.updateContent(newContent);

        qnaPersistenceManager.persist(updatedQna);
    }
}

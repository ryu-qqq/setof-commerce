package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.CreateOrderQnaCommand;
import com.ryuqq.setof.application.qna.factory.command.QnaCommandFactory;
import com.ryuqq.setof.application.qna.manager.command.QnaPersistenceManager;
import com.ryuqq.setof.application.qna.port.in.command.CreateOrderQnaUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import org.springframework.stereotype.Service;

/**
 * 주문 문의 생성 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnaCommandFactory로 Qna 도메인 생성 (이미지 포함, VO 검증)
 *   <li>QnaPersistenceManager로 저장 (ID 자동 생성)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateOrderQnaService implements CreateOrderQnaUseCase {

    private final QnaCommandFactory qnaCommandFactory;
    private final QnaPersistenceManager qnaPersistenceManager;

    public CreateOrderQnaService(
            QnaCommandFactory qnaCommandFactory, QnaPersistenceManager qnaPersistenceManager) {
        this.qnaCommandFactory = qnaCommandFactory;
        this.qnaPersistenceManager = qnaPersistenceManager;
    }

    @Override
    public Long execute(CreateOrderQnaCommand command) {
        Qna qna = qnaCommandFactory.createOrderQna(command);
        QnaId savedId = qnaPersistenceManager.persist(qna);
        return savedId.getValue();
    }
}

package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.CreateProductQnaCommand;
import com.ryuqq.setof.application.qna.factory.command.QnaCommandFactory;
import com.ryuqq.setof.application.qna.manager.command.QnaPersistenceManager;
import com.ryuqq.setof.application.qna.port.in.command.CreateProductQnaUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import org.springframework.stereotype.Service;

/**
 * 상품 문의 생성 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnaCommandFactory로 Qna 도메인 생성 (VO 검증 포함)
 *   <li>QnaPersistenceManager로 저장 (ID 자동 생성)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateProductQnaService implements CreateProductQnaUseCase {

    private final QnaCommandFactory qnaCommandFactory;
    private final QnaPersistenceManager qnaPersistenceManager;

    public CreateProductQnaService(
            QnaCommandFactory qnaCommandFactory, QnaPersistenceManager qnaPersistenceManager) {
        this.qnaCommandFactory = qnaCommandFactory;
        this.qnaPersistenceManager = qnaPersistenceManager;
    }

    @Override
    public Long execute(CreateProductQnaCommand command) {
        Qna qna = qnaCommandFactory.createProductQna(command);
        QnaId savedId = qnaPersistenceManager.persist(qna);
        return savedId.getValue();
    }
}

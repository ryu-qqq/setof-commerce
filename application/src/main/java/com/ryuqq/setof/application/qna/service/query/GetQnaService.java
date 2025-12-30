package com.ryuqq.setof.application.qna.service.query;

import com.ryuqq.setof.application.qna.assembler.QnaAssembler;
import com.ryuqq.setof.application.qna.dto.response.QnaResponse;
import com.ryuqq.setof.application.qna.manager.query.QnaReadManager;
import com.ryuqq.setof.application.qna.port.in.query.GetQnaUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import org.springframework.stereotype.Service;

/**
 * 문의 단건 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnaReadManager로 QnA 조회 (없으면 예외)
 *   <li>QnaAssembler로 Response DTO 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetQnaService implements GetQnaUseCase {

    private final QnaReadManager qnaReadManager;
    private final QnaAssembler qnaAssembler;

    public GetQnaService(QnaReadManager qnaReadManager, QnaAssembler qnaAssembler) {
        this.qnaReadManager = qnaReadManager;
        this.qnaAssembler = qnaAssembler;
    }

    @Override
    public QnaResponse execute(Long qnaId) {
        Qna qna = qnaReadManager.findById(qnaId);
        return qnaAssembler.toQnaResponse(qna);
    }
}

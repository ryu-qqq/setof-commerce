package com.ryuqq.setof.application.qna.service.query;

import com.ryuqq.setof.application.qna.assembler.QnaAssembler;
import com.ryuqq.setof.application.qna.dto.response.QnaReplyResponse;
import com.ryuqq.setof.application.qna.manager.query.QnaReplyReadManager;
import com.ryuqq.setof.application.qna.port.in.query.GetQnaRepliesUseCase;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 문의 답변 목록 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnaReplyReadManager로 Reply 목록 조회 (Materialized Path 순서)
 *   <li>QnaAssembler로 Response DTO 목록 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetQnaRepliesService implements GetQnaRepliesUseCase {

    private final QnaReplyReadManager qnaReplyReadManager;
    private final QnaAssembler qnaAssembler;

    public GetQnaRepliesService(
            QnaReplyReadManager qnaReplyReadManager, QnaAssembler qnaAssembler) {
        this.qnaReplyReadManager = qnaReplyReadManager;
        this.qnaAssembler = qnaAssembler;
    }

    @Override
    public List<QnaReplyResponse> execute(Long qnaId) {
        List<QnaReply> replies = qnaReplyReadManager.findByQnaId(qnaId);
        return qnaAssembler.toQnaReplyResponses(replies);
    }
}

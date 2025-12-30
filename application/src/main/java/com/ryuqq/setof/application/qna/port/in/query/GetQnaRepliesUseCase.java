package com.ryuqq.setof.application.qna.port.in.query;

import com.ryuqq.setof.application.qna.dto.response.QnaReplyResponse;
import java.util.List;

/**
 * Get QnA Replies UseCase (Query)
 *
 * <p>문의 답변 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetQnaRepliesUseCase {

    /**
     * 문의 답변 목록 조회 실행
     *
     * @param qnaId 문의 ID
     * @return 답변 목록 (Materialized Path 순서로 정렬)
     */
    List<QnaReplyResponse> execute(Long qnaId);
}

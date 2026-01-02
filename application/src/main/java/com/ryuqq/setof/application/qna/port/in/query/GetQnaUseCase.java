package com.ryuqq.setof.application.qna.port.in.query;

import com.ryuqq.setof.application.qna.dto.response.QnaResponse;

/**
 * Get QnA UseCase (Query)
 *
 * <p>문의 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetQnaUseCase {

    /**
     * 문의 단건 조회 실행
     *
     * @param qnaId 문의 ID
     * @return 문의 상세 응답
     */
    QnaResponse execute(Long qnaId);
}

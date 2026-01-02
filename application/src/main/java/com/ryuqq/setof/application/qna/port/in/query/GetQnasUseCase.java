package com.ryuqq.setof.application.qna.port.in.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.qna.dto.query.QnaSearchQuery;
import com.ryuqq.setof.application.qna.dto.response.QnaSummaryResponse;

/**
 * Get QnAs UseCase (Query)
 *
 * <p>문의 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetQnasUseCase {

    /**
     * 문의 목록 조회 실행
     *
     * @param query 문의 검색 조건
     * @return 문의 목록 페이지 응답
     */
    PageResponse<QnaSummaryResponse> execute(QnaSearchQuery query);
}

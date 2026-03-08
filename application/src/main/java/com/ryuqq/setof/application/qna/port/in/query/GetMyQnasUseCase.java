package com.ryuqq.setof.application.qna.port.in.query;

import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;

/**
 * GetMyQnasUseCase - 내 Q&A 목록 조회 UseCase.
 *
 * <p>레거시 GET /api/v1/qna/my-page 기반. Cursor 기반 페이지네이션으로 내 Q&A 목록 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetMyQnasUseCase {

    MyQnaSliceResult execute(MyQnaSearchParams params);
}

package com.ryuqq.setof.application.qna.port.in.query;

import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;

/**
 * GetProductQnasUseCase - 상품 Q&A 목록 조회 UseCase.
 *
 * <p>레거시 GET /api/v1/qna/product/{productGroupId} 기반. Offset 기반 페이지네이션으로 상품 Q&A 목록 조회. 비밀글 마스킹 및
 * 작성자명 마스킹 적용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetProductQnasUseCase {

    QnaPageResult execute(ProductQnaSearchParams params);
}

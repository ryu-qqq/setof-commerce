package com.ryuqq.setof.application.qna.port.out.query;

import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import java.util.List;
import java.util.Optional;

/**
 * QnaProductQueryPort - 상품 Q&A 조회 출력 포트.
 *
 * <p>상품에 걸린 Q&A 목록/상세 조회를 담당합니다.
 * 2-step 쿼리(ID 조회 → 상세 조회)는 Adapter 내부에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaProductQueryPort {

    List<QnaWithAnswersResult> fetchProductQnas(ProductQnaSearchCriteria criteria);

    long countProductQnas(ProductQnaSearchCriteria criteria);

    Optional<QnaWithAnswersResult> findProductQnaById(long qnaId);
}

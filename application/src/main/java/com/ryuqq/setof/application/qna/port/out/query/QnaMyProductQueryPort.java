package com.ryuqq.setof.application.qna.port.out.query;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import java.util.List;

/**
 * QnaMyProductQueryPort - 내 상품 Q&A 조회 출력 포트.
 *
 * <p>PRODUCT 타입 내 Q&A 조회 시 상품 정보(상품그룹, 브랜드, 이미지) JOIN이 필요한 복합 조회 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaMyProductQueryPort {

    List<MyQnaResult> fetchMyProductQnas(QnaSearchCriteria criteria);
}

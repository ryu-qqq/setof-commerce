package com.ryuqq.setof.application.qna.port.out.query;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import java.util.List;

/**
 * QnaMyOrderQueryPort - 내 주문 Q&A 조회 출력 포트.
 *
 * <p>ORDER 타입 내 Q&A 조회 시 주문 정보(주문, 결제, 옵션 스냅샷) + 상품 정보 JOIN이 필요한 복합 조회 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaMyOrderQueryPort {

    List<MyQnaResult> fetchMyOrderQnas(QnaSearchCriteria criteria);
}

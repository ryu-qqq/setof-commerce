package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.util.List;

/**
 * MyQnaReadStrategy - 내 Q&A 조회 전략 인터페이스.
 *
 * <p>QnaType별 조회 로직을 분리합니다. PRODUCT/ORDER 각각 다른 JOIN 구조를 가집니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface MyQnaReadStrategy {

    /** 이 전략이 처리하는 QnaType. */
    QnaType supportType();

    /** criteria 기반으로 내 Q&A 목록을 조회합니다. */
    List<MyQnaResult> fetchMyQnas(QnaSearchCriteria criteria);
}

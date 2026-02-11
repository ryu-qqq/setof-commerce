package com.ryuqq.setof.application.faq.port.in.query;

import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import java.util.List;

/**
 * FAQ 유형별 조회 UseCase.
 *
 * <p>faqType 기반으로 FAQ 목록을 조회합니다. 페이지네이션 없이 전체 목록을 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetFaqsByTypeUseCase {

    /**
     * FAQ 유형별 목록을 조회합니다.
     *
     * @param params 검색 파라미터
     * @return FAQ 결과 목록
     */
    List<FaqResult> execute(FaqSearchParams params);
}

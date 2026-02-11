package com.ryuqq.setof.application.faq.dto.query;

import com.ryuqq.setof.domain.faq.vo.FaqType;

/**
 * FAQ 검색 파라미터.
 *
 * <p>FAQ는 faqType 기반 필터링이 핵심이며, 페이지네이션 없이 전체 목록을 반환합니다.
 *
 * @param faqType FAQ 유형 (필수)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FaqSearchParams(FaqType faqType) {

    public static FaqSearchParams of(FaqType faqType) {
        return new FaqSearchParams(faqType);
    }
}

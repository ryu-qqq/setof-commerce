package com.ryuqq.setof.domain.legacy.faq.dto.query;

import com.ryuqq.setof.domain.legacy.faq.FaqType;

/**
 * LegacyFaqSearchCondition - 레거시 FAQ 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param faqType FAQ 유형
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyFaqSearchCondition(FaqType faqType) {

    /**
     * FAQ 유형으로 조회하는 생성자.
     *
     * @param faqType FAQ 유형
     * @return LegacyFaqSearchCondition
     */
    public static LegacyFaqSearchCondition of(FaqType faqType) {
        return new LegacyFaqSearchCondition(faqType);
    }

    /**
     * TOP FAQ 조회용.
     *
     * @return LegacyFaqSearchCondition
     */
    public static LegacyFaqSearchCondition ofTop() {
        return new LegacyFaqSearchCondition(FaqType.TOP);
    }

    /**
     * TOP 유형 여부.
     *
     * @return TOP이면 true
     */
    public boolean isTop() {
        return faqType == FaqType.TOP;
    }
}

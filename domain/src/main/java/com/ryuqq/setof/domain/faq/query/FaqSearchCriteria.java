package com.ryuqq.setof.domain.faq.query;

import com.ryuqq.setof.domain.faq.vo.FaqType;

/**
 * FaqSearchCriteria - FAQ 검색 조건 Criteria.
 *
 * <p>DOM-CRI-001: Record로 정의 + of() 팩토리 메서드.
 *
 * <p>FAQ는 faqType 기반 필터링이 핵심이며, 페이지네이션 없이 전체 목록을 반환합니다. 정렬은 faqType에 따라 자동 결정됩니다:
 *
 * <ul>
 *   <li>TOP: topDisplayOrder ASC
 *   <li>그 외: displayOrder ASC
 * </ul>
 *
 * @param faqType FAQ 유형 (필수)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FaqSearchCriteria(FaqType faqType) {

    /** Compact Constructor - null 방어 */
    public FaqSearchCriteria {
        if (faqType == null) {
            throw new IllegalArgumentException("faqType은 필수입니다");
        }
    }

    /**
     * FAQ 유형으로 검색 조건 생성.
     *
     * @param faqType FAQ 유형
     * @return FaqSearchCriteria
     */
    public static FaqSearchCriteria of(FaqType faqType) {
        return new FaqSearchCriteria(faqType);
    }

    /**
     * TOP FAQ 검색 조건 생성.
     *
     * @return FaqSearchCriteria (TOP)
     */
    public static FaqSearchCriteria ofTop() {
        return new FaqSearchCriteria(FaqType.TOP);
    }

    /** TOP 유형 여부. */
    public boolean isTop() {
        return faqType.isTop();
    }
}

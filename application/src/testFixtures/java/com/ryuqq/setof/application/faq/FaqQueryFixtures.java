package com.ryuqq.setof.application.faq;

import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.domain.faq.vo.FaqType;

/**
 * Faq Application Query 테스트 Fixtures.
 *
 * <p>Faq 관련 Query 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class FaqQueryFixtures {

    private FaqQueryFixtures() {}

    // ===== FaqSearchParams =====

    public static FaqSearchParams searchParams() {
        return FaqSearchParams.of(FaqType.MEMBER_LOGIN);
    }

    public static FaqSearchParams searchParams(FaqType faqType) {
        return FaqSearchParams.of(faqType);
    }

    public static FaqSearchParams topSearchParams() {
        return FaqSearchParams.of(FaqType.TOP);
    }

    public static FaqSearchParams shippingSearchParams() {
        return FaqSearchParams.of(FaqType.SHIPPING);
    }

    // ===== FaqResult =====

    public static FaqResult faqResult(Long faqId) {
        return FaqResult.of(faqId, FaqType.MEMBER_LOGIN, "테스트 제목", "테스트 내용입니다.", 1, null);
    }

    public static FaqResult faqResult(Long faqId, FaqType faqType) {
        return FaqResult.of(faqId, faqType, "테스트 제목", "테스트 내용입니다.", 1, null);
    }

    public static FaqResult topFaqResult(Long faqId) {
        return FaqResult.of(faqId, FaqType.TOP, "상단 고정 FAQ 제목", "상단 고정 FAQ 내용입니다.", 1, 1);
    }
}

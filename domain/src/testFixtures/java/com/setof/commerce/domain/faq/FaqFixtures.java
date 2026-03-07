package com.ryuqq.setof.domain.faq;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.id.FaqId;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqContents;
import com.ryuqq.setof.domain.faq.vo.FaqDisplayOrder;
import com.ryuqq.setof.domain.faq.vo.FaqTitle;
import com.ryuqq.setof.domain.faq.vo.FaqType;

/**
 * Faq 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Faq 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class FaqFixtures {

    private FaqFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_TITLE = "배송은 얼마나 걸리나요?";
    public static final String DEFAULT_CONTENTS = "일반적으로 2~3 영업일 이내에 배송됩니다.";
    public static final int DEFAULT_DISPLAY_ORDER = 1;
    public static final int DEFAULT_TOP_DISPLAY_ORDER = 1;

    // ===== ID Fixtures =====
    public static FaqId defaultFaqId() {
        return FaqId.of(1L);
    }

    public static FaqId faqId(Long value) {
        return FaqId.of(value);
    }

    public static FaqId newFaqId() {
        return FaqId.forNew();
    }

    // ===== VO Fixtures =====
    public static FaqTitle defaultFaqTitle() {
        return FaqTitle.of(DEFAULT_TITLE);
    }

    public static FaqTitle faqTitle(String value) {
        return FaqTitle.of(value);
    }

    public static FaqContents defaultFaqContents() {
        return FaqContents.of(DEFAULT_CONTENTS);
    }

    public static FaqContents faqContents(String value) {
        return FaqContents.of(value);
    }

    public static FaqDisplayOrder defaultFaqDisplayOrder() {
        return FaqDisplayOrder.of(DEFAULT_DISPLAY_ORDER);
    }

    public static FaqDisplayOrder faqDisplayOrder(int value) {
        return FaqDisplayOrder.of(value);
    }

    // ===== Aggregate Fixtures =====

    /** 일반 FAQ (SHIPPING 유형, topDisplayOrder 없음). */
    public static Faq shippingFaq() {
        return Faq.reconstitute(
                defaultFaqId(),
                FaqType.SHIPPING,
                defaultFaqTitle(),
                defaultFaqContents(),
                defaultFaqDisplayOrder(),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 일반 FAQ (특정 ID 지정). */
    public static Faq shippingFaq(Long id) {
        return Faq.reconstitute(
                FaqId.of(id),
                FaqType.SHIPPING,
                defaultFaqTitle(),
                defaultFaqContents(),
                defaultFaqDisplayOrder(),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** TOP FAQ (topDisplayOrder 있음). */
    public static Faq topFaq() {
        return Faq.reconstitute(
                FaqId.of(2L),
                FaqType.TOP,
                FaqTitle.of("자주 묻는 질문 1위"),
                FaqContents.of("상단 고정 FAQ 내용입니다."),
                FaqDisplayOrder.defaultOrder(),
                DEFAULT_TOP_DISPLAY_ORDER,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 주문/결제 유형 FAQ. */
    public static Faq orderPaymentFaq() {
        return Faq.reconstitute(
                FaqId.of(3L),
                FaqType.ORDER_PAYMENT,
                FaqTitle.of("결제 방법은 어떻게 되나요?"),
                FaqContents.of("신용카드, 계좌이체, 간편결제를 지원합니다."),
                FaqDisplayOrder.of(2),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== SearchCriteria Fixtures =====
    public static FaqSearchCriteria shippingSearchCriteria() {
        return FaqSearchCriteria.of(FaqType.SHIPPING);
    }

    public static FaqSearchCriteria topSearchCriteria() {
        return FaqSearchCriteria.ofTop();
    }
}

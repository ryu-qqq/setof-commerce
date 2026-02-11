package com.ryuqq.setof.application.legacy.faq.dto.response;

import com.ryuqq.setof.domain.faq.vo.FaqType;

/**
 * LegacyFaqResult - 레거시 FAQ 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param faqType FAQ 유형
 * @param title 제목
 * @param contents 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyFaqResult(FaqType faqType, String title, String contents) {

    /**
     * 팩토리 메서드.
     *
     * @param faqType FAQ 유형
     * @param title 제목
     * @param contents 내용
     * @return LegacyFaqResult
     */
    public static LegacyFaqResult of(FaqType faqType, String title, String contents) {
        return new LegacyFaqResult(faqType, title, contents);
    }
}

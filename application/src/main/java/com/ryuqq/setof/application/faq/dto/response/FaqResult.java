package com.ryuqq.setof.application.faq.dto.response;

import com.ryuqq.setof.domain.faq.vo.FaqType;

/**
 * FAQ 조회 결과.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param faqId FAQ ID
 * @param faqType FAQ 유형
 * @param title 제목
 * @param contents 내용
 * @param displayOrder 표시 순서
 * @param topDisplayOrder 상단 고정 순서 (null이면 일반 FAQ)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FaqResult(
        Long faqId,
        FaqType faqType,
        String title,
        String contents,
        int displayOrder,
        Integer topDisplayOrder) {

    public static FaqResult of(
            Long faqId,
            FaqType faqType,
            String title,
            String contents,
            int displayOrder,
            Integer topDisplayOrder) {
        return new FaqResult(faqId, faqType, title, contents, displayOrder, topDisplayOrder);
    }
}

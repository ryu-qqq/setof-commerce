package com.ryuqq.setof.application.faq.assembler;

import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Faq Assembler.
 *
 * <p>Domain → Result 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FaqAssembler {

    /**
     * Domain → FaqResult 변환.
     *
     * @param faq Faq 도메인 객체
     * @return FaqResult
     */
    public FaqResult toResult(Faq faq) {
        return FaqResult.of(
                faq.idValue(),
                faq.faqType(),
                faq.titleValue(),
                faq.contentsValue(),
                faq.displayOrderValue(),
                faq.topDisplayOrder());
    }

    /**
     * Domain List → FaqResult List 변환.
     *
     * @param faqs Faq 도메인 객체 목록
     * @return FaqResult 목록
     */
    public List<FaqResult> toResults(List<Faq> faqs) {
        return faqs.stream().map(this::toResult).toList();
    }
}

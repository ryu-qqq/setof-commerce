package com.ryuqq.setof.application.faq.assembler;

import com.ryuqq.setof.application.faq.dto.response.FaqResponse;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * FAQ Assembler
 *
 * <p>Domain → Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqAssembler {

    /**
     * Faq 도메인 → FaqResponse 변환
     *
     * @param faq FAQ 도메인
     * @return FaqResponse DTO
     */
    public FaqResponse toResponse(Faq faq) {
        return FaqResponse.of(
                faq.getIdValue(),
                faq.getCategoryCodeValue(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getIsTop(),
                faq.getTopOrder(),
                faq.getDisplayOrder(),
                faq.getStatus().name(),
                faq.getViewCount(),
                faq.getCreatedAt(),
                faq.getUpdatedAt());
    }

    /**
     * Faq 목록 → FaqResponse 목록 변환
     *
     * @param faqs FAQ 목록
     * @return FaqResponse 목록
     */
    public List<FaqResponse> toResponses(List<Faq> faqs) {
        return faqs.stream().map(this::toResponse).toList();
    }
}

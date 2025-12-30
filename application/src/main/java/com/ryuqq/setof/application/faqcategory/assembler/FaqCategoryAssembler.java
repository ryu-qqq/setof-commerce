package com.ryuqq.setof.application.faqcategory.assembler;

import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * FAQ 카테고리 Assembler
 *
 * <p>FAQ 카테고리 도메인 객체를 응답 DTO로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqCategoryAssembler {

    /**
     * FAQ 카테고리를 응답 DTO로 변환
     *
     * @param category FAQ 카테고리
     * @return FAQ 카테고리 응답
     */
    public FaqCategoryResponse toResponse(FaqCategory category) {
        return new FaqCategoryResponse(
                category.getId().value(),
                category.getCodeValue(),
                category.getName(),
                category.getDescription(),
                category.getDisplayOrder(),
                category.getStatus().name(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }

    /**
     * FAQ 카테고리 목록을 응답 DTO 목록으로 변환
     *
     * @param categories FAQ 카테고리 목록
     * @return FAQ 카테고리 응답 목록
     */
    public List<FaqCategoryResponse> toResponseList(List<FaqCategory> categories) {
        return categories.stream().map(this::toResponse).toList();
    }
}

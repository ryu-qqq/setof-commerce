package com.ryuqq.setof.application.faqcategory.dto.command;

/**
 * FAQ 카테고리 활성화 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record ActivateFaqCategoryCommand(Long categoryId, Long updatedBy) {

    public ActivateFaqCategoryCommand {
        if (categoryId == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수입니다");
        }
    }
}

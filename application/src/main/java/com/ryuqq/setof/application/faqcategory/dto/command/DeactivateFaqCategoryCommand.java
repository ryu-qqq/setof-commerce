package com.ryuqq.setof.application.faqcategory.dto.command;

/**
 * FAQ 카테고리 비활성화 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record DeactivateFaqCategoryCommand(Long categoryId, Long updatedBy) {

    public DeactivateFaqCategoryCommand {
        if (categoryId == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수입니다");
        }
    }
}

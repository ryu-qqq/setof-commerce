package com.ryuqq.setof.application.faqcategory.dto.command;

/**
 * FAQ 카테고리 삭제 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record DeleteFaqCategoryCommand(Long categoryId, Long deletedBy) {

    public DeleteFaqCategoryCommand {
        if (categoryId == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수입니다");
        }
    }
}

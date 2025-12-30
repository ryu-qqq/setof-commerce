package com.ryuqq.setof.application.faqcategory.dto.command;

/**
 * FAQ 카테고리 생성 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record CreateFaqCategoryCommand(
        String code, String name, String description, int displayOrder, Long createdBy) {

    public CreateFaqCategoryCommand {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("카테고리 코드는 필수입니다");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리명은 필수입니다");
        }
        if (displayOrder < 0) {
            throw new IllegalArgumentException("표시 순서는 0 이상이어야 합니다");
        }
    }
}

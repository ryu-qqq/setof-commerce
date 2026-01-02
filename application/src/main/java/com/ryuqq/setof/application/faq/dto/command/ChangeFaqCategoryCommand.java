package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 카테고리 변경 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record ChangeFaqCategoryCommand(Long faqId, String newCategoryCode, Long updatedBy) {

    public ChangeFaqCategoryCommand {
        if (faqId == null) {
            throw new IllegalArgumentException("FAQ ID는 필수입니다");
        }
        if (newCategoryCode == null || newCategoryCode.isBlank()) {
            throw new IllegalArgumentException("새 카테고리 코드는 필수입니다");
        }
    }
}

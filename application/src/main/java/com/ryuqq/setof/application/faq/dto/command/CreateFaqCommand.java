package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 생성 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record CreateFaqCommand(
        String categoryCode, String question, String answer, int displayOrder, Long createdBy) {

    public CreateFaqCommand {
        if (categoryCode == null || categoryCode.isBlank()) {
            throw new IllegalArgumentException("카테고리 코드는 필수입니다");
        }
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("질문은 필수입니다");
        }
        if (answer == null || answer.isBlank()) {
            throw new IllegalArgumentException("답변은 필수입니다");
        }
        if (displayOrder < 0) {
            throw new IllegalArgumentException("표시 순서는 0 이상이어야 합니다");
        }
    }
}

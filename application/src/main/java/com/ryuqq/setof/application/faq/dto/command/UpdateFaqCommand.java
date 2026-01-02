package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 수정 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record UpdateFaqCommand(
        Long faqId, String question, String answer, int displayOrder, Long updatedBy) {

    public UpdateFaqCommand {
        if (faqId == null) {
            throw new IllegalArgumentException("FAQ ID는 필수입니다");
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

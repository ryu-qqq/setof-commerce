package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 상단 노출 해제 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record UnsetTopFaqCommand(Long faqId, Long updatedBy) {

    public UnsetTopFaqCommand {
        if (faqId == null) {
            throw new IllegalArgumentException("FAQ ID는 필수입니다");
        }
    }
}

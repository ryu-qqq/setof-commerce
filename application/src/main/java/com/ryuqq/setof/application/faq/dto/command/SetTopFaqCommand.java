package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 상단 노출 설정 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record SetTopFaqCommand(Long faqId, int topOrder, Long updatedBy) {

    public SetTopFaqCommand {
        if (faqId == null) {
            throw new IllegalArgumentException("FAQ ID는 필수입니다");
        }
        if (topOrder < 1) {
            throw new IllegalArgumentException("상단 순서는 1 이상이어야 합니다");
        }
    }
}

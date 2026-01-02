package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 숨김 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record HideFaqCommand(Long faqId, Long updatedBy) {

    public HideFaqCommand {
        if (faqId == null) {
            throw new IllegalArgumentException("FAQ ID는 필수입니다");
        }
    }
}

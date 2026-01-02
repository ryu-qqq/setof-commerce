package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 삭제 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record DeleteFaqCommand(Long faqId, Long deletedBy) {

    public DeleteFaqCommand {
        if (faqId == null) {
            throw new IllegalArgumentException("FAQ ID는 필수입니다");
        }
    }
}

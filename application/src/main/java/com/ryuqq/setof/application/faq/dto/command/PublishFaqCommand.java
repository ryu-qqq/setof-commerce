package com.ryuqq.setof.application.faq.dto.command;

/**
 * FAQ 게시 Command
 *
 * @author development-team
 * @since 1.0.0
 */
public record PublishFaqCommand(Long faqId, Long updatedBy) {

    public PublishFaqCommand {
        if (faqId == null) {
            throw new IllegalArgumentException("FAQ ID는 필수입니다");
        }
    }
}

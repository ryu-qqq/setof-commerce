package com.ryuqq.setof.application.faqcategory.port.in.command;

import com.ryuqq.setof.application.faqcategory.dto.command.UpdateFaqCategoryCommand;

/**
 * FAQ 카테고리 수정 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateFaqCategoryUseCase {

    /**
     * FAQ 카테고리 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateFaqCategoryCommand command);
}

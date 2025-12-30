package com.ryuqq.setof.application.faqcategory.port.in.command;

import com.ryuqq.setof.application.faqcategory.dto.command.DeleteFaqCategoryCommand;

/**
 * FAQ 카테고리 삭제 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteFaqCategoryUseCase {

    /**
     * FAQ 카테고리 삭제 (Soft Delete)
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteFaqCategoryCommand command);
}

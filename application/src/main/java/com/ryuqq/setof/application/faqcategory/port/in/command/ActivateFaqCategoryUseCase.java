package com.ryuqq.setof.application.faqcategory.port.in.command;

import com.ryuqq.setof.application.faqcategory.dto.command.ActivateFaqCategoryCommand;

/**
 * FAQ 카테고리 활성화 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ActivateFaqCategoryUseCase {

    /**
     * FAQ 카테고리 활성화
     *
     * @param command 활성화 커맨드
     */
    void execute(ActivateFaqCategoryCommand command);
}

package com.ryuqq.setof.application.faqcategory.port.in.command;

import com.ryuqq.setof.application.faqcategory.dto.command.DeactivateFaqCategoryCommand;

/**
 * FAQ 카테고리 비활성화 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeactivateFaqCategoryUseCase {

    /**
     * FAQ 카테고리 비활성화
     *
     * @param command 비활성화 커맨드
     */
    void execute(DeactivateFaqCategoryCommand command);
}

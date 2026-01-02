package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.ChangeFaqCategoryCommand;

/**
 * FAQ 카테고리 변경 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ChangeFaqCategoryUseCase {

    /**
     * FAQ 카테고리 변경
     *
     * @param command 카테고리 변경 커맨드
     */
    void execute(ChangeFaqCategoryCommand command);
}

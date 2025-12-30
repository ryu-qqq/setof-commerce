package com.ryuqq.setof.application.faqcategory.port.in.command;

import com.ryuqq.setof.application.faqcategory.dto.command.CreateFaqCategoryCommand;

/**
 * FAQ 카테고리 생성 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateFaqCategoryUseCase {

    /**
     * FAQ 카테고리 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 FAQ 카테고리 ID
     */
    Long execute(CreateFaqCategoryCommand command);
}

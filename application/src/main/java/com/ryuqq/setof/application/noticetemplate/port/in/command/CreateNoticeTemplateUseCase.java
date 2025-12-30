package com.ryuqq.setof.application.noticetemplate.port.in.command;

import com.ryuqq.setof.application.noticetemplate.dto.command.CreateNoticeTemplateCommand;

/**
 * 상품고시 템플릿 생성 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateNoticeTemplateUseCase {

    /**
     * 상품고시 템플릿 생성
     *
     * @param command 생성 Command
     * @return 생성된 템플릿 ID
     */
    Long create(CreateNoticeTemplateCommand command);
}

package com.ryuqq.setof.application.noticetemplate.port.in.command;

import com.ryuqq.setof.application.noticetemplate.dto.command.UpdateNoticeTemplateCommand;

/**
 * 상품고시 템플릿 수정 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateNoticeTemplateUseCase {

    /**
     * 상품고시 템플릿 수정
     *
     * @param command 수정 Command
     */
    void update(UpdateNoticeTemplateCommand command);
}

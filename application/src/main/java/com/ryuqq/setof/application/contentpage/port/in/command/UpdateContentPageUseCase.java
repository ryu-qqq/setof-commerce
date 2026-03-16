package com.ryuqq.setof.application.contentpage.port.in.command;

import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;

/**
 * UpdateContentPageUseCase - 콘텐츠 페이지 수정 UseCase.
 *
 * <p>APP-UCS-001: UseCase는 interface이며 단일 execute 메서드를 갖습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateContentPageUseCase {

    /**
     * 콘텐츠 페이지를 수정합니다.
     *
     * @param command 콘텐츠 페이지 수정 Command
     */
    void execute(UpdateContentPageCommand command);
}

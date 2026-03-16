package com.ryuqq.setof.application.contentpage.port.in.command;

import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;

/**
 * ChangeContentPageStatusUseCase - 콘텐츠 페이지 노출 상태 변경 UseCase.
 *
 * <p>APP-UCS-001: UseCase는 interface이며 단일 execute 메서드를 갖습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ChangeContentPageStatusUseCase {

    /**
     * 콘텐츠 페이지의 노출 상태를 변경합니다.
     *
     * @param command 노출 상태 변경 Command
     */
    void execute(ChangeContentPageStatusCommand command);
}

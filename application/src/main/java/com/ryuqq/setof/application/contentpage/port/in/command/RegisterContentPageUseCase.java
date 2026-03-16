package com.ryuqq.setof.application.contentpage.port.in.command;

import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;

/**
 * RegisterContentPageUseCase - 콘텐츠 페이지 등록 UseCase.
 *
 * <p>APP-UCS-001: UseCase는 interface이며 단일 execute 메서드를 갖습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterContentPageUseCase {

    /**
     * 콘텐츠 페이지를 등록합니다.
     *
     * @param command 콘텐츠 페이지 등록 Command
     * @return 생성된 콘텐츠 페이지 ID
     */
    Long execute(RegisterContentPageCommand command);
}

package com.ryuqq.setof.application.content.port.in.command;

import com.ryuqq.setof.application.content.dto.command.CreateContentCommand;

/**
 * Content 생성 UseCase (Command)
 *
 * <p>콘텐츠 생성을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateContentUseCase {

    /**
     * 콘텐츠 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 Content ID
     */
    Long execute(CreateContentCommand command);
}

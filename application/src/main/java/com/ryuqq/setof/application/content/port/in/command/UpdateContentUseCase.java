package com.ryuqq.setof.application.content.port.in.command;

import com.ryuqq.setof.application.content.dto.command.UpdateContentCommand;

/**
 * Content 수정 UseCase (Command)
 *
 * <p>콘텐츠 수정을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateContentUseCase {

    /**
     * 콘텐츠 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateContentCommand command);
}

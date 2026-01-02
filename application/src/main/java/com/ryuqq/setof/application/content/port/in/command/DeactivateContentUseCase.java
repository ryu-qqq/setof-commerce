package com.ryuqq.setof.application.content.port.in.command;

import com.ryuqq.setof.application.content.dto.command.DeactivateContentCommand;

/**
 * Content 비활성화 UseCase (Command)
 *
 * <p>콘텐츠 비활성화를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeactivateContentUseCase {

    /**
     * 콘텐츠 비활성화
     *
     * @param command 비활성화 커맨드
     */
    void execute(DeactivateContentCommand command);
}

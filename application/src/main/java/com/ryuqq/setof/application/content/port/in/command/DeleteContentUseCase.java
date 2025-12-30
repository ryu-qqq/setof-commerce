package com.ryuqq.setof.application.content.port.in.command;

import com.ryuqq.setof.application.content.dto.command.DeleteContentCommand;

/**
 * Content 삭제 UseCase (Command)
 *
 * <p>콘텐츠 삭제(소프트 삭제)를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteContentUseCase {

    /**
     * 콘텐츠 삭제
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteContentCommand command);
}

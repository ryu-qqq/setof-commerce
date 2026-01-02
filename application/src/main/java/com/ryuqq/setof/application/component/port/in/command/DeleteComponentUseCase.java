package com.ryuqq.setof.application.component.port.in.command;

import com.ryuqq.setof.application.component.dto.command.DeleteComponentCommand;

/**
 * Component 삭제 UseCase (Command)
 *
 * <p>컴포넌트 삭제(소프트 삭제)를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteComponentUseCase {

    /**
     * 컴포넌트 삭제
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteComponentCommand command);
}

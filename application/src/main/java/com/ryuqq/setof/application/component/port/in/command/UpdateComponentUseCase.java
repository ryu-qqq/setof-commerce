package com.ryuqq.setof.application.component.port.in.command;

import com.ryuqq.setof.application.component.dto.command.UpdateComponentCommand;

/**
 * Component 수정 UseCase (Command)
 *
 * <p>컴포넌트 수정을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateComponentUseCase {

    /**
     * 컴포넌트 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateComponentCommand command);
}

package com.ryuqq.setof.application.component.port.in.command;

import com.ryuqq.setof.application.component.dto.command.CreateComponentCommand;

/**
 * Component 생성 UseCase (Command)
 *
 * <p>컴포넌트 생성을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateComponentUseCase {

    /**
     * 컴포넌트 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 Component ID
     */
    Long execute(CreateComponentCommand command);
}

package com.ryuqq.setof.application.componentitem.port.in.command;

import com.ryuqq.setof.application.componentitem.dto.command.CreateComponentItemCommand;
import java.util.List;

/**
 * ComponentItem 생성 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateComponentItemUseCase {

    /**
     * ComponentItem 생성
     *
     * @param command 생성 명령
     * @return 생성된 ComponentItem ID
     */
    Long create(CreateComponentItemCommand command);

    /**
     * ComponentItem 목록 일괄 생성
     *
     * @param commands 생성 명령 목록
     * @return 생성된 ComponentItem ID 목록
     */
    List<Long> createAll(List<CreateComponentItemCommand> commands);
}

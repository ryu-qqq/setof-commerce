package com.ryuqq.setof.application.commoncodetype.port.in.command;

import com.ryuqq.setof.application.commoncodetype.dto.command.UpdateCommonCodeTypeCommand;

/** 공통 코드 타입 수정 UseCase. */
public interface UpdateCommonCodeTypeUseCase {

    void execute(UpdateCommonCodeTypeCommand command);
}

package com.ryuqq.setof.application.commoncodetype.port.in.command;

import com.ryuqq.setof.application.commoncodetype.dto.command.ChangeActiveStatusCommand;

/** 공통 코드 타입 활성화 상태 변경 UseCase. */
public interface ChangeCommonCodeTypeStatusUseCase {

    void execute(ChangeActiveStatusCommand command);
}

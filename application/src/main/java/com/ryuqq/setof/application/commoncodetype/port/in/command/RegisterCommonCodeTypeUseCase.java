package com.ryuqq.setof.application.commoncodetype.port.in.command;

import com.ryuqq.setof.application.commoncodetype.dto.command.RegisterCommonCodeTypeCommand;

/** 공통 코드 타입 등록 UseCase. */
public interface RegisterCommonCodeTypeUseCase {

    Long execute(RegisterCommonCodeTypeCommand command);
}

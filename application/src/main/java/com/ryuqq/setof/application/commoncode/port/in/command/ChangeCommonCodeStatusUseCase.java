package com.ryuqq.setof.application.commoncode.port.in.command;

import com.ryuqq.setof.application.commoncode.dto.command.ChangeCommonCodeStatusCommand;

/** 공통 코드 활성화 상태 변경 UseCase. */
public interface ChangeCommonCodeStatusUseCase {

    void execute(ChangeCommonCodeStatusCommand command);
}

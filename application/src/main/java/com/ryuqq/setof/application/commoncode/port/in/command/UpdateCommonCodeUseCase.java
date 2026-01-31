package com.ryuqq.setof.application.commoncode.port.in.command;

import com.ryuqq.setof.application.commoncode.dto.command.UpdateCommonCodeCommand;

/** 공통 코드 수정 UseCase. */
public interface UpdateCommonCodeUseCase {

    void execute(UpdateCommonCodeCommand command);
}

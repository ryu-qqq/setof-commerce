package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.UpdateBannerCommand;

/**
 * Banner 수정 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateBannerUseCase {

    /**
     * 배너 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateBannerCommand command);
}

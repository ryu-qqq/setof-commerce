package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.DeleteBannerCommand;

/**
 * Banner 삭제 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteBannerUseCase {

    /**
     * 배너 삭제
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteBannerCommand command);
}

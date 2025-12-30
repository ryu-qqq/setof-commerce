package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.CreateBannerCommand;

/**
 * Banner 생성 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateBannerUseCase {

    /**
     * 배너 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 Banner ID
     */
    Long execute(CreateBannerCommand command);
}

package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.DeactivateBannerCommand;

/**
 * 배너 비활성화 UseCase
 *
 * <p>활성화된 배너를 비활성화 상태로 변경합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface DeactivateBannerUseCase {

    /**
     * 배너 비활성화 실행
     *
     * @param command 비활성화 명령
     */
    void execute(DeactivateBannerCommand command);
}

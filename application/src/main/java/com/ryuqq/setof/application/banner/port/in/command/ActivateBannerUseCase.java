package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.ActivateBannerCommand;

/**
 * 배너 활성화 UseCase
 *
 * <p>비활성화된 배너를 활성화 상태로 변경합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ActivateBannerUseCase {

    /**
     * 배너 활성화 실행
     *
     * @param command 활성화 명령
     */
    void execute(ActivateBannerCommand command);
}

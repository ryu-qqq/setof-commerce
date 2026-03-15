package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;

/**
 * ChangeBannerGroupStatusUseCase - 배너 그룹 노출 상태 변경 UseCase.
 *
 * <p>APP-UCS-001: UseCase는 interface이며 단일 execute 메서드를 갖습니다. Service가 이 인터페이스를 구현하며,
 * Adapter-In(Controller)이 의존합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ChangeBannerGroupStatusUseCase {

    /**
     * 배너 그룹의 노출 상태를 변경합니다.
     *
     * @param command 배너 그룹 노출 상태 변경 Command
     */
    void execute(ChangeBannerGroupStatusCommand command);
}

package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;

/**
 * RegisterBannerGroupUseCase - 배너 그룹 등록 UseCase.
 *
 * <p>APP-UCS-001: UseCase는 interface이며 단일 execute 메서드를 갖습니다. Service가 이 인터페이스를 구현하며,
 * Adapter-In(Controller)이 의존합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterBannerGroupUseCase {

    /**
     * 배너 그룹을 등록합니다.
     *
     * @param command 배너 그룹 등록 Command
     * @return 생성된 배너 그룹 ID
     */
    Long execute(RegisterBannerGroupCommand command);
}

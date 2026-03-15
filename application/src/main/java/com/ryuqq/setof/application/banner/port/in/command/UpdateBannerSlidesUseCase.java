package com.ryuqq.setof.application.banner.port.in.command;

import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;

/**
 * UpdateBannerSlidesUseCase - 배너 슬라이드 일괄 수정 UseCase.
 *
 * <p>APP-UCS-001: UseCase는 interface이며 단일 execute 메서드를 갖습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateBannerSlidesUseCase {
    void execute(UpdateBannerSlidesCommand command);
}

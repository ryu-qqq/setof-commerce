package com.ryuqq.setof.application.navigation.port.in.command;

import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;

/**
 * RegisterNavigationMenuUseCase - 네비게이션 메뉴 등록 입력 포트.
 *
 * <p>네비게이션 메뉴 신규 등록을 위한 입력 포트 인터페이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterNavigationMenuUseCase {

    /**
     * 네비게이션 메뉴를 등록합니다.
     *
     * @param command 등록 Command
     * @return 생성된 네비게이션 메뉴 ID
     */
    Long execute(RegisterNavigationMenuCommand command);
}

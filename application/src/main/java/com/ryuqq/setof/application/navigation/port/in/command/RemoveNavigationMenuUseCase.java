package com.ryuqq.setof.application.navigation.port.in.command;

import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;

/**
 * RemoveNavigationMenuUseCase - 네비게이션 메뉴 삭제 입력 포트.
 *
 * <p>네비게이션 메뉴 논리 삭제를 위한 입력 포트 인터페이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RemoveNavigationMenuUseCase {

    /**
     * 네비게이션 메뉴를 논리 삭제합니다.
     *
     * @param command 삭제 Command
     */
    void execute(RemoveNavigationMenuCommand command);
}

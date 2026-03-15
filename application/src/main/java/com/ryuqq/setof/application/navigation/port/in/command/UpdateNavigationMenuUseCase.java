package com.ryuqq.setof.application.navigation.port.in.command;

import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;

/**
 * UpdateNavigationMenuUseCase - 네비게이션 메뉴 수정 입력 포트.
 *
 * <p>네비게이션 메뉴 수정을 위한 입력 포트 인터페이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateNavigationMenuUseCase {

    /**
     * 네비게이션 메뉴를 수정합니다.
     *
     * @param command 수정 Command
     */
    void execute(UpdateNavigationMenuCommand command);
}

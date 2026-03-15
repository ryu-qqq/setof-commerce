package com.ryuqq.setof.application.navigation.factory;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenuUpdateData;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuCommandFactory - 네비게이션 메뉴 Command Factory.
 *
 * <p>Command DTO를 Domain 객체 또는 Context로 변환합니다.
 *
 * <p>APP-TIM-001: TimeProvider.now()는 Factory에서만 호출합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationMenuCommandFactory {

    private final TimeProvider timeProvider;

    public NavigationMenuCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * RegisterNavigationMenuCommand로부터 NavigationMenu 도메인 객체를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()는 Factory에서만 호출합니다.
     *
     * @param command 등록 Command
     * @return NavigationMenu 도메인 객체
     */
    public NavigationMenu create(RegisterNavigationMenuCommand command) {
        Instant now = timeProvider.now();

        DisplayPeriod displayPeriod =
                DisplayPeriod.of(command.displayStartAt(), command.displayEndAt());

        return NavigationMenu.forNew(
                command.title(),
                command.linkUrl(),
                command.displayOrder(),
                displayPeriod,
                command.active(),
                now);
    }

    /**
     * UpdateNavigationMenuCommand로부터 UpdateContext를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()는 Factory에서만 호출합니다.
     *
     * @param command 수정 Command
     * @return UpdateContext (NavigationMenuId, NavigationMenuUpdateData, 변경 시각)
     */
    public UpdateContext<NavigationMenuId, NavigationMenuUpdateData> createUpdateContext(
            UpdateNavigationMenuCommand command) {
        Instant now = timeProvider.now();

        NavigationMenuId id = NavigationMenuId.of(command.id());
        DisplayPeriod displayPeriod =
                DisplayPeriod.of(command.displayStartAt(), command.displayEndAt());
        NavigationMenuUpdateData updateData =
                new NavigationMenuUpdateData(
                        command.title(),
                        command.linkUrl(),
                        command.displayOrder(),
                        displayPeriod,
                        command.active(),
                        now);

        return new UpdateContext<>(id, updateData, now);
    }

    /**
     * RemoveNavigationMenuCommand로부터 StatusChangeContext를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()는 Factory에서만 호출합니다.
     *
     * @param command 삭제 Command
     * @return StatusChangeContext (NavigationMenuId, 변경 시각)
     */
    public StatusChangeContext<NavigationMenuId> createRemoveContext(
            RemoveNavigationMenuCommand command) {
        Instant now = timeProvider.now();
        NavigationMenuId id = NavigationMenuId.of(command.id());
        return new StatusChangeContext<>(id, now);
    }
}

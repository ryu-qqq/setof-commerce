package com.ryuqq.setof.application.contentpage.factory;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageRegistrationBundle;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageUpdateBundle;
import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterDisplayComponentCommand;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.ViewExtensionCommand;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPageUpdateData;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import com.ryuqq.setof.domain.contentpage.vo.BadgeType;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.ListType;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtension;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtensionType;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ContentPageCommandFactory - 콘텐츠 페이지 Command Factory.
 *
 * <p>APP-TIM-001: TimeProvider.now() 호출은 Factory에서만 허용합니다.
 *
 * <p>순수 생성 로직만 담당합니다. 조회는 Service/Manager에서 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentPageCommandFactory {

    private final TimeProvider timeProvider;

    public ContentPageCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * 등록 Command로 ContentPageRegistrationBundle을 생성합니다.
     *
     * @param command 콘텐츠 페이지 등록 Command
     * @return 등록 번들
     */
    public ContentPageRegistrationBundle createRegistrationBundle(
            RegisterContentPageCommand command) {
        ContentPage contentPage = createContentPage(command);
        return new ContentPageRegistrationBundle(contentPage, command.components());
    }

    /**
     * 수정 번들을 생성합니다.
     *
     * <p>Service에서 조회한 기존 ContentPage + Components를 받아 Command와 함께 번들로 묶습니다.
     *
     * @param contentPage 기존 콘텐츠 페이지
     * @param existingComponents 기존 디스플레이 컴포넌트 목록
     * @param command 수정 Command
     * @return 수정 번들
     */
    public ContentPageUpdateBundle createUpdateBundle(
            ContentPage contentPage,
            List<DisplayComponent> existingComponents,
            UpdateContentPageCommand command) {
        Instant now = timeProvider.now();
        return new ContentPageUpdateBundle(
                contentPage, existingComponents, command.components(), now);
    }

    /**
     * 노출 상태 변경 Command로 StatusChangeContext를 생성합니다.
     *
     * @param command 노출 상태 변경 Command
     * @return StatusChangeContext
     */
    public StatusChangeContext<ContentPageId> createStatusChangeContext(
            ChangeContentPageStatusCommand command) {
        Instant now = timeProvider.now();
        return new StatusChangeContext<>(ContentPageId.of(command.id()), now);
    }

    /**
     * 컴포넌트 Command 목록으로 신규 DisplayComponent 도메인 객체 목록을 생성합니다.
     *
     * @param contentPageId 소속 콘텐츠 페이지 ID
     * @param commands 컴포넌트 Command 목록
     * @return DisplayComponent 목록
     */
    public List<DisplayComponent> createComponents(
            long contentPageId, List<RegisterDisplayComponentCommand> commands) {
        Instant now = timeProvider.now();
        return commands.stream().map(cmd -> toDisplayComponent(contentPageId, cmd, now)).toList();
    }

    /**
     * 수정용 컴포넌트 Command 목록으로 DisplayComponent 목록을 생성합니다.
     *
     * @param contentPageId 소속 콘텐츠 페이지 ID
     * @param commands 컴포넌트 Command 목록
     * @param now 수정 시각 (번들에서 전달)
     * @return DisplayComponent 목록 (diff 계산용 incoming)
     */
    public List<DisplayComponent> createComponentsForUpdate(
            long contentPageId, List<RegisterDisplayComponentCommand> commands, Instant now) {
        return commands.stream().map(cmd -> toDisplayComponent(contentPageId, cmd, now)).toList();
    }

    /**
     * 수정 Command로 ContentPageUpdateData를 생성합니다.
     *
     * @param command 콘텐츠 페이지 수정 Command
     * @param now 수정 시각 (번들에서 전달)
     * @return ContentPageUpdateData
     */
    public ContentPageUpdateData createUpdateData(UpdateContentPageCommand command, Instant now) {
        return new ContentPageUpdateData(
                command.title(),
                command.memo(),
                command.imageUrl(),
                DisplayPeriod.of(command.displayStartAt(), command.displayEndAt()),
                command.active(),
                now);
    }

    private ContentPage createContentPage(RegisterContentPageCommand command) {
        Instant now = timeProvider.now();
        return ContentPage.forNew(
                command.title(),
                command.memo(),
                command.imageUrl(),
                DisplayPeriod.of(command.displayStartAt(), command.displayEndAt()),
                command.active(),
                now);
    }

    private DisplayComponent toDisplayComponent(
            long contentPageId, RegisterDisplayComponentCommand cmd, Instant now) {

        ComponentType componentType = ComponentType.valueOf(cmd.componentType());
        DisplayConfig displayConfig =
                new DisplayConfig(
                        parseEnum(ListType.class, cmd.listType(), ListType.NONE),
                        parseEnum(OrderType.class, cmd.orderType(), OrderType.NONE),
                        parseEnum(BadgeType.class, cmd.badgeType(), BadgeType.NONE),
                        cmd.filterEnabled());
        DisplayPeriod displayPeriod = DisplayPeriod.of(cmd.displayStartAt(), cmd.displayEndAt());
        ViewExtension viewExtension = toViewExtension(cmd);

        return DisplayComponent.forNew(
                contentPageId,
                cmd.componentName(),
                cmd.displayOrder(),
                componentType,
                displayConfig,
                displayPeriod,
                cmd.active(),
                viewExtension,
                null,
                now);
    }

    private ViewExtension toViewExtension(RegisterDisplayComponentCommand cmd) {
        ViewExtensionCommand vec = cmd.viewExtensionCommand();
        if (vec == null) {
            return null;
        }
        return new ViewExtension(
                0L,
                parseEnum(ViewExtensionType.class, vec.viewExtensionType(), ViewExtensionType.NONE),
                vec.linkUrl(),
                vec.buttonName(),
                vec.productCountPerClick(),
                vec.maxClickCount(),
                parseEnum(
                        ViewExtensionType.class, vec.afterMaxActionType(), ViewExtensionType.NONE),
                vec.afterMaxActionLinkUrl());
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value, E defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}

package com.ryuqq.setof.application.banner.factory;

import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroupUpdateData;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerGroupCommandFactory - 배너 그룹 Command Factory.
 *
 * <p>APP-TIM-001: TimeProvider.now() 호출은 Factory에서만 허용합니다. Service나 Manager에서는 직접 시간을 생성하지 않습니다.
 *
 * <p>Factory의 책임:
 *
 * <ul>
 *   <li>create() - Command → Domain 객체 생성
 *   <li>createUpdateContext() - Command → UpdateContext 생성
 *   <li>createStatusChangeContext() - Command → StatusChangeContext 생성
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerGroupCommandFactory {

    private final TimeProvider timeProvider;

    public BannerGroupCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * 배너 그룹 등록 Command로 BannerGroup 도메인 객체를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 배너 그룹 등록 Command
     * @return 신규 BannerGroup 도메인 객체
     */
    public BannerGroup create(RegisterBannerGroupCommand command) {
        Instant now = timeProvider.now();
        List<BannerSlide> slides = command.slides().stream().map(s -> toNewSlide(s, now)).toList();
        return BannerGroup.forNew(
                command.title(),
                BannerType.valueOf(command.bannerType()),
                DisplayPeriod.of(command.displayStartAt(), command.displayEndAt()),
                command.active(),
                slides,
                now);
    }

    /**
     * 배너 그룹 수정 Command로 UpdateContext를 생성합니다 (슬라이드 미포함).
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * <p>그룹 정보만 수정하며 슬라이드는 포함하지 않습니다. 슬라이드 수정은 {@link
     * #createSlideUpdateContext(UpdateBannerSlidesCommand)}를 사용하세요.
     *
     * @param command 배너 그룹 수정 Command
     * @return UpdateContext (BannerGroupId, BannerGroupUpdateData, changedAt)
     */
    public UpdateContext<BannerGroupId, BannerGroupUpdateData> createUpdateContext(
            UpdateBannerGroupCommand command) {
        Instant now = timeProvider.now();
        BannerGroupUpdateData updateData =
                new BannerGroupUpdateData(
                        command.title(),
                        BannerType.valueOf(command.bannerType()),
                        DisplayPeriod.of(command.displayStartAt(), command.displayEndAt()),
                        command.active(),
                        List.of(),
                        now);
        return new UpdateContext<>(BannerGroupId.of(command.id()), updateData, now);
    }

    /**
     * 배너 슬라이드 일괄 수정 Command로 UpdateContext를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 배너 슬라이드 수정 Command
     * @return UpdateContext (BannerGroupId, SlideEntry 목록, changedAt)
     */
    public UpdateContext<BannerGroupId, List<BannerGroupUpdateData.SlideEntry>>
            createSlideUpdateContext(UpdateBannerSlidesCommand command) {
        Instant now = timeProvider.now();
        List<BannerGroupUpdateData.SlideEntry> slideEntries =
                command.slides().stream()
                        .map(
                                s ->
                                        new BannerGroupUpdateData.SlideEntry(
                                                s.slideId(),
                                                s.title(),
                                                s.imageUrl(),
                                                s.linkUrl(),
                                                s.displayOrder(),
                                                DisplayPeriod.of(
                                                        s.displayStartAt(), s.displayEndAt()),
                                                s.active()))
                        .toList();
        return new UpdateContext<>(BannerGroupId.of(command.bannerGroupId()), slideEntries, now);
    }

    /**
     * 배너 그룹 노출 상태 변경 Command로 StatusChangeContext를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 배너 그룹 노출 상태 변경 Command
     * @return StatusChangeContext (BannerGroupId, changedAt)
     */
    public StatusChangeContext<BannerGroupId> createStatusChangeContext(
            ChangeBannerGroupStatusCommand command) {
        Instant now = timeProvider.now();
        return new StatusChangeContext<>(BannerGroupId.of(command.id()), now);
    }

    /**
     * 배너 그룹 삭제 Command로 StatusChangeContext를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 배너 그룹 삭제 Command
     * @return StatusChangeContext (BannerGroupId, changedAt)
     */
    public StatusChangeContext<BannerGroupId> createRemoveContext(
            RemoveBannerGroupCommand command) {
        Instant now = timeProvider.now();
        return new StatusChangeContext<>(BannerGroupId.of(command.id()), now);
    }

    private BannerSlide toNewSlide(RegisterBannerSlideCommand s, Instant now) {
        return BannerSlide.forNew(
                s.title(),
                s.imageUrl(),
                s.linkUrl(),
                s.displayOrder(),
                DisplayPeriod.of(s.displayStartAt(), s.displayEndAt()),
                s.active(),
                now);
    }
}

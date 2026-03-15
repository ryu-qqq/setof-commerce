package com.ryuqq.setof.application.banner;

import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import java.time.Instant;
import java.util.List;

/**
 * BannerGroup Application Command 테스트 Fixtures.
 *
 * <p>BannerGroup 등록/수정/상태변경/삭제 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerCommandFixtures {

    private BannerCommandFixtures() {}

    // ===== 공통 상수 =====
    private static final Instant DISPLAY_START_AT = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant DISPLAY_END_AT = Instant.parse("2025-12-31T23:59:59Z");

    // ===== RegisterBannerSlideCommand =====

    public static RegisterBannerSlideCommand registerSlideCommand() {
        return new RegisterBannerSlideCommand(
                "테스트 슬라이드",
                "https://example.com/banner.png",
                "https://example.com/link",
                1,
                DISPLAY_START_AT,
                DISPLAY_END_AT,
                true);
    }

    public static RegisterBannerSlideCommand registerSlideCommand(String title, int displayOrder) {
        return new RegisterBannerSlideCommand(
                title,
                "https://example.com/banner.png",
                "https://example.com/link",
                displayOrder,
                DISPLAY_START_AT,
                DISPLAY_END_AT,
                true);
    }

    public static List<RegisterBannerSlideCommand> registerSlideCommands() {
        return List.of(registerSlideCommand("슬라이드 1", 1), registerSlideCommand("슬라이드 2", 2));
    }

    // ===== RegisterBannerGroupCommand =====

    public static RegisterBannerGroupCommand registerCommand() {
        return new RegisterBannerGroupCommand(
                "테스트 배너 그룹",
                "RECOMMEND",
                DISPLAY_START_AT,
                DISPLAY_END_AT,
                true,
                registerSlideCommands());
    }

    public static RegisterBannerGroupCommand registerCommand(String title, String bannerType) {
        return new RegisterBannerGroupCommand(
                title, bannerType, DISPLAY_START_AT, DISPLAY_END_AT, true, registerSlideCommands());
    }

    public static RegisterBannerGroupCommand registerCommandWithInactiveStatus() {
        return new RegisterBannerGroupCommand(
                "비활성 배너 그룹",
                "CATEGORY",
                DISPLAY_START_AT,
                DISPLAY_END_AT,
                false,
                List.of(registerSlideCommand()));
    }

    // ===== UpdateBannerSlideCommand =====

    public static UpdateBannerSlideCommand updateSlideCommand(Long slideId) {
        return new UpdateBannerSlideCommand(
                slideId,
                "수정된 슬라이드",
                "https://example.com/new-banner.png",
                "https://example.com/new-link",
                1,
                DISPLAY_START_AT,
                DISPLAY_END_AT,
                true);
    }

    public static UpdateBannerSlideCommand newSlideCommand() {
        return new UpdateBannerSlideCommand(
                null,
                "신규 슬라이드",
                "https://example.com/new-banner.png",
                "https://example.com/new-link",
                2,
                DISPLAY_START_AT,
                DISPLAY_END_AT,
                true);
    }

    public static List<UpdateBannerSlideCommand> updateSlideCommands() {
        return List.of(updateSlideCommand(1L), newSlideCommand());
    }

    // ===== UpdateBannerGroupCommand =====

    public static UpdateBannerGroupCommand updateCommand(long id) {
        return new UpdateBannerGroupCommand(
                id, "수정된 배너 그룹", "CATEGORY", DISPLAY_START_AT, DISPLAY_END_AT, false);
    }

    public static UpdateBannerGroupCommand updateCommandInactive(long id) {
        return new UpdateBannerGroupCommand(
                id, "수정된 배너 그룹", "RECOMMEND", DISPLAY_START_AT, DISPLAY_END_AT, false);
    }

    // ===== UpdateBannerSlidesCommand =====

    public static UpdateBannerSlidesCommand updateSlidesCommand(long bannerGroupId) {
        return new UpdateBannerSlidesCommand(bannerGroupId, updateSlideCommands());
    }

    public static UpdateBannerSlidesCommand updateSlidesCommandWithAllNewSlides(
            long bannerGroupId) {
        return new UpdateBannerSlidesCommand(bannerGroupId, List.of(newSlideCommand()));
    }

    // ===== ChangeBannerGroupStatusCommand =====

    public static ChangeBannerGroupStatusCommand activateCommand(long id) {
        return new ChangeBannerGroupStatusCommand(id, true);
    }

    public static ChangeBannerGroupStatusCommand deactivateCommand(long id) {
        return new ChangeBannerGroupStatusCommand(id, false);
    }

    // ===== RemoveBannerGroupCommand =====

    public static RemoveBannerGroupCommand removeCommand(long id) {
        return new RemoveBannerGroupCommand(id);
    }
}

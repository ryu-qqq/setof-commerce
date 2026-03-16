package com.ryuqq.setof.application.contentpage;

import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterDisplayComponentCommand;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.ViewExtensionCommand;
import java.time.Instant;
import java.util.List;

/**
 * ContentPage Application Command 테스트 Fixtures.
 *
 * <p>ContentPageCommandFactory, ContentPageCommandFacade, ContentPageCommandManager,
 * RegisterContentPageService, UpdateContentPageService, ChangeContentPageStatusService 테스트에서 공통으로
 * 사용하는 커맨드 관련 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ContentPageCommandFixtures {

    private ContentPageCommandFixtures() {}

    // ===== 공통 시간 상수 =====

    public static final Instant DISPLAY_START = Instant.parse("2025-01-01T00:00:00Z");
    public static final Instant DISPLAY_END = Instant.parse("2099-12-31T23:59:59Z");

    // ===== RegisterContentPageCommand =====

    public static RegisterContentPageCommand registerCommand() {
        return new RegisterContentPageCommand(
                "테스트 콘텐츠 페이지",
                "테스트 메모",
                "https://example.com/image.jpg",
                DISPLAY_START,
                DISPLAY_END,
                true,
                List.of(registerDisplayComponentCommand()));
    }

    public static RegisterContentPageCommand registerCommandWithoutComponents() {
        return new RegisterContentPageCommand(
                "컴포넌트 없는 콘텐츠 페이지",
                "메모 없음",
                "https://example.com/no-component.jpg",
                DISPLAY_START,
                DISPLAY_END,
                true,
                List.of());
    }

    public static RegisterContentPageCommand registerCommandWithMultipleComponents() {
        return new RegisterContentPageCommand(
                "멀티 컴포넌트 콘텐츠 페이지",
                "멀티 컴포넌트 메모",
                "https://example.com/multi.jpg",
                DISPLAY_START,
                DISPLAY_END,
                true,
                List.of(
                        registerDisplayComponentCommand(),
                        registerDisplayComponentCommandWithViewExtension()));
    }

    // ===== UpdateContentPageCommand =====

    public static UpdateContentPageCommand updateCommand(long contentPageId) {
        return new UpdateContentPageCommand(
                contentPageId,
                "수정된 콘텐츠 페이지",
                "수정된 메모",
                "https://example.com/updated.jpg",
                DISPLAY_START,
                DISPLAY_END,
                true,
                List.of(registerDisplayComponentCommand()));
    }

    public static UpdateContentPageCommand updateCommandWithoutComponents(long contentPageId) {
        return new UpdateContentPageCommand(
                contentPageId,
                "수정된 콘텐츠 페이지",
                "수정된 메모",
                "https://example.com/updated.jpg",
                DISPLAY_START,
                DISPLAY_END,
                false,
                List.of());
    }

    // ===== ChangeContentPageStatusCommand =====

    public static ChangeContentPageStatusCommand activateCommand(long contentPageId) {
        return new ChangeContentPageStatusCommand(contentPageId, true);
    }

    public static ChangeContentPageStatusCommand deactivateCommand(long contentPageId) {
        return new ChangeContentPageStatusCommand(contentPageId, false);
    }

    // ===== RegisterDisplayComponentCommand =====

    public static RegisterDisplayComponentCommand registerDisplayComponentCommand() {
        return new RegisterDisplayComponentCommand(
                null,
                1,
                "PRODUCT",
                "상품 컴포넌트",
                DISPLAY_START,
                DISPLAY_END,
                true,
                "TWO_STEP",
                "RECOMMEND",
                "NONE",
                false,
                20,
                null,
                null);
    }

    public static RegisterDisplayComponentCommand registerDisplayComponentCommandWithId(
            Long componentId) {
        return new RegisterDisplayComponentCommand(
                componentId,
                1,
                "PRODUCT",
                "기존 상품 컴포넌트",
                DISPLAY_START,
                DISPLAY_END,
                true,
                "TWO_STEP",
                "RECOMMEND",
                "NONE",
                false,
                20,
                null,
                null);
    }

    public static RegisterDisplayComponentCommand
            registerDisplayComponentCommandWithViewExtension() {
        return new RegisterDisplayComponentCommand(
                null,
                2,
                "PRODUCT",
                "뷰 확장 컴포넌트",
                DISPLAY_START,
                DISPLAY_END,
                true,
                "TWO_STEP",
                "NONE",
                "NONE",
                false,
                10,
                viewExtensionCommand(),
                null);
    }

    // ===== ViewExtensionCommand =====

    public static ViewExtensionCommand viewExtensionCommand() {
        return new ViewExtensionCommand(
                "MORE_BUTTON",
                "https://example.com/more",
                "더보기",
                5,
                3,
                "LINK",
                "https://example.com/after");
    }
}

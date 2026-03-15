package com.ryuqq.setof.application.navigation;

import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import java.time.Instant;

public final class NavigationCommandFixtures {
    private NavigationCommandFixtures() {}

    public static final Instant DEFAULT_START = Instant.parse("2025-01-01T00:00:00Z");
    public static final Instant DEFAULT_END = Instant.parse("2025-12-31T23:59:59Z");

    public static RegisterNavigationMenuCommand registerCommand() {
        return new RegisterNavigationMenuCommand(
                "홈", "https://example.com/home", 1, DEFAULT_START, DEFAULT_END, true);
    }

    public static RegisterNavigationMenuCommand registerCommand(String title, String linkUrl) {
        return new RegisterNavigationMenuCommand(
                title, linkUrl, 1, DEFAULT_START, DEFAULT_END, true);
    }

    public static UpdateNavigationMenuCommand updateCommand() {
        return new UpdateNavigationMenuCommand(
                1L, "남성", "https://example.com/men", 2, DEFAULT_START, DEFAULT_END, false);
    }

    public static UpdateNavigationMenuCommand updateCommand(long id) {
        return new UpdateNavigationMenuCommand(
                id, "남성", "https://example.com/men", 2, DEFAULT_START, DEFAULT_END, false);
    }

    public static RemoveNavigationMenuCommand removeCommand() {
        return new RemoveNavigationMenuCommand(1L);
    }

    public static RemoveNavigationMenuCommand removeCommand(long id) {
        return new RemoveNavigationMenuCommand(id);
    }
}

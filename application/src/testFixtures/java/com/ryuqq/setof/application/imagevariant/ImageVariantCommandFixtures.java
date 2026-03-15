package com.ryuqq.setof.application.imagevariant;

import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand.VariantCommand;
import java.util.List;

/**
 * ImageVariant Application Command 테스트 Fixtures.
 *
 * <p>이미지 Variant 동기화 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ImageVariantCommandFixtures {

    private ImageVariantCommandFixtures() {}

    // ===== SyncImageVariantsCommand =====

    public static SyncImageVariantsCommand syncCommand() {
        return new SyncImageVariantsCommand(100L, "PRODUCT_GROUP_IMAGE", variantCommands());
    }

    public static SyncImageVariantsCommand syncCommand(Long sourceImageId) {
        return new SyncImageVariantsCommand(
                sourceImageId, "PRODUCT_GROUP_IMAGE", variantCommands());
    }

    public static SyncImageVariantsCommand syncCommandWithDescriptionImage() {
        return new SyncImageVariantsCommand(200L, "DESCRIPTION_IMAGE", variantCommands());
    }

    public static SyncImageVariantsCommand syncCommandWithEmptyVariants() {
        return new SyncImageVariantsCommand(100L, "PRODUCT_GROUP_IMAGE", List.of());
    }

    public static SyncImageVariantsCommand syncCommandWithSingleVariant() {
        return new SyncImageVariantsCommand(
                100L, "PRODUCT_GROUP_IMAGE", List.of(mediumWebpVariantCommand()));
    }

    // ===== VariantCommand =====

    public static List<VariantCommand> variantCommands() {
        return List.of(
                smallWebpVariantCommand(), mediumWebpVariantCommand(), largeWebpVariantCommand());
    }

    public static VariantCommand smallWebpVariantCommand() {
        return new VariantCommand(
                "SMALL_WEBP",
                "asset-small-001",
                "https://cdn.example.com/images/300x300.webp",
                300,
                300);
    }

    public static VariantCommand mediumWebpVariantCommand() {
        return new VariantCommand(
                "MEDIUM_WEBP",
                "asset-medium-001",
                "https://cdn.example.com/images/600x600.webp",
                600,
                600);
    }

    public static VariantCommand largeWebpVariantCommand() {
        return new VariantCommand(
                "LARGE_WEBP",
                "asset-large-001",
                "https://cdn.example.com/images/1200x1200.webp",
                1200,
                1200);
    }

    public static VariantCommand originalWebpVariantCommand() {
        return new VariantCommand(
                "ORIGINAL_WEBP",
                "asset-original-001",
                "https://cdn.example.com/images/original.webp",
                null,
                null);
    }
}

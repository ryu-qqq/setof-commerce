package com.ryuqq.setof.application.productgroup;

import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroup Application Command 테스트 Fixtures.
 *
 * <p>ProductGroup 등록/수정 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupCommandFixtures {

    private ProductGroupCommandFixtures() {}

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    // ===== RegisterProductGroupCommand =====

    public static RegisterProductGroupCommand registerCommand() {
        return new RegisterProductGroupCommand(
                null,
                1L,
                1L,
                1L,
                1L,
                1L,
                "테스트 상품그룹",
                "SINGLE",
                50000,
                45000,
                List.of(
                        new RegisterProductGroupCommand.ImageCommand(
                                "THUMBNAIL", "http://example.com/thumb.jpg", 1)),
                List.of(
                        new RegisterProductGroupCommand.OptionGroupCommand(
                                "색상",
                                1,
                                List.of(
                                        new RegisterProductGroupCommand.OptionValueCommand("블랙", 1),
                                        new RegisterProductGroupCommand.OptionValueCommand(
                                                "화이트", 2)))),
                List.of(
                        new RegisterProductGroupCommand.ProductCommand(
                                null,
                                "SKU-001",
                                50000,
                                45000,
                                10,
                                1,
                                List.of(new SelectedOption("색상", "블랙")))),
                new RegisterProductGroupCommand.DescriptionCommand(
                        "<p>상품 상세설명</p>",
                        List.of(
                                new RegisterProductGroupCommand.DescriptionImageCommand(
                                        "http://example.com/desc.jpg", 1))),
                new RegisterProductGroupCommand.NoticeCommand(
                        List.of(
                                new RegisterProductGroupCommand.NoticeEntryCommand(
                                        1L, "소재", "면 100%"))));
    }

    public static RegisterProductGroupCommand registerCommandWithoutOptions() {
        return new RegisterProductGroupCommand(
                null,
                1L,
                1L,
                1L,
                1L,
                1L,
                "옵션없는 상품그룹",
                "NONE",
                30000,
                25000,
                List.of(
                        new RegisterProductGroupCommand.ImageCommand(
                                "THUMBNAIL", "http://example.com/thumb.jpg", 1)),
                List.of(),
                List.of(
                        new RegisterProductGroupCommand.ProductCommand(
                                null, "SKU-NONE-001", 30000, 25000, 5, 1, List.of())),
                null,
                null);
    }

    // ===== UpdateProductGroupFullCommand =====

    public static UpdateProductGroupFullCommand updateFullCommand(long productGroupId) {
        return new UpdateProductGroupFullCommand(
                productGroupId,
                "수정된 상품그룹",
                2L,
                2L,
                2L,
                2L,
                "COMBINATION",
                60000,
                55000,
                List.of(
                        new UpdateProductGroupFullCommand.ImageCommand(
                                "THUMBNAIL", "http://example.com/new-thumb.jpg", 1)),
                List.of(
                        new UpdateProductGroupFullCommand.OptionGroupCommand(
                                10L,
                                "색상",
                                1,
                                List.of(
                                        new UpdateProductGroupFullCommand.OptionValueCommand(
                                                100L, "블랙", 1),
                                        new UpdateProductGroupFullCommand.OptionValueCommand(
                                                null, "그린", 3)))),
                List.of(
                        new UpdateProductGroupFullCommand.ProductCommand(
                                1L,
                                "SKU-001",
                                60000,
                                55000,
                                8,
                                1,
                                List.of(new SelectedOption("색상", "블랙")))),
                new UpdateProductGroupFullCommand.DescriptionCommand("<p>수정된 상세설명</p>", List.of()),
                new UpdateProductGroupFullCommand.NoticeCommand(
                        List.of(
                                new UpdateProductGroupFullCommand.NoticeEntryCommand(
                                        1L, "소재", "폴리에스터 100%"))));
    }

    // ===== UpdateProductGroupBasicInfoCommand =====

    public static UpdateProductGroupBasicInfoCommand updateBasicInfoCommand(long productGroupId) {
        return new UpdateProductGroupBasicInfoCommand(
                productGroupId, "수정된 기본정보 상품그룹", 2L, 2L, 2L, 2L);
    }

    // ===== Bundle Fixtures =====

    public static ProductGroupRegistrationBundle registrationBundle() {
        RegisterProductGroupCommand command = registerCommand();
        return new ProductGroupRegistrationBundle(
                ProductGroupFixtures.activeProductGroup(),
                command.images().stream()
                        .map(
                                img ->
                                        new com.ryuqq.setof.application.productgroupimage.dto
                                                .command.RegisterProductGroupImagesCommand
                                                .ImageCommand(
                                                img.imageType(), img.imageUrl(), img.sortOrder()))
                        .toList(),
                command.optionType(),
                command.optionGroups(),
                command.description() != null ? command.description().content() : null,
                command.description() != null
                        ? command.description().descriptionImages()
                        : List.of(),
                command.notice() != null && command.notice().entries() != null
                        ? command.notice().entries().stream()
                                .map(
                                        e ->
                                                new com.ryuqq.setof.application.productnotice.dto
                                                        .command.RegisterProductNoticeCommand
                                                        .NoticeEntryCommand(
                                                        e.noticeFieldId(),
                                                        e.fieldName(),
                                                        e.fieldValue()))
                                .toList()
                        : List.of(),
                command.products().stream()
                        .map(
                                p ->
                                        new com.ryuqq.setof.application.product.dto.command
                                                .RegisterProductsCommand.ProductData(
                                                p.productId(),
                                                p.skuCode(),
                                                p.regularPrice(),
                                                p.currentPrice(),
                                                p.stockQuantity(),
                                                p.sortOrder(),
                                                p.selectedOptions()))
                        .toList(),
                FIXED_NOW);
    }

    public static ProductGroupUpdateBundle updateBundle(long productGroupId) {
        return new ProductGroupUpdateBundle(
                ProductGroupFixtures.activeProductGroup(productGroupId),
                updateFullCommand(productGroupId),
                FIXED_NOW);
    }
}

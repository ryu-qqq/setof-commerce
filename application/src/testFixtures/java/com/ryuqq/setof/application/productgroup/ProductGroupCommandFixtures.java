package com.ryuqq.setof.application.productgroup;

import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.product.dto.command.RegisterProductsCommand;
import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupFullCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.selleroption.dto.command.RegisterSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroup Application Command 테스트 Fixtures.
 *
 * <p>ProductGroup 관련 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupCommandFixtures {

    private ProductGroupCommandFixtures() {}

    // ===== RegisterProductGroupFullCommand Fixtures =====

    public static RegisterProductGroupFullCommand registerFullCommand() {
        return new RegisterProductGroupFullCommand(
                1L,
                1L,
                1L,
                1L,
                1L,
                "테스트 상품그룹",
                "SINGLE",
                50000,
                45000,
                List.of(imageCommand("THUMBNAIL", "https://example.com/thumb.png", 1)),
                List.of(optionGroupCommand("색상", List.of(optionValueCommand("검정", 1)))),
                List.of(productCommand(0, 10, 1, List.of())),
                descriptionCommand("상품 설명 내용", null),
                noticeCommand(List.of(noticeEntryCommand("소재", "면 100%", 1))));
    }

    public static RegisterProductGroupFullCommand registerFullCommandWithNullOptionals() {
        return new RegisterProductGroupFullCommand(
                1L,
                null,
                null,
                null,
                null,
                "테스트 상품그룹",
                "NONE",
                50000,
                45000,
                List.of(),
                List.of(),
                List.of(),
                null,
                null);
    }

    public static RegisterProductGroupFullCommand.ImageCommand imageCommand(
            String imageType, String imageUrl, int sortOrder) {
        return new RegisterProductGroupFullCommand.ImageCommand(imageType, imageUrl, sortOrder);
    }

    public static RegisterProductGroupFullCommand.OptionGroupCommand optionGroupCommand(
            String name, List<RegisterProductGroupFullCommand.OptionValueCommand> values) {
        return new RegisterProductGroupFullCommand.OptionGroupCommand(name, values);
    }

    public static RegisterProductGroupFullCommand.OptionValueCommand optionValueCommand(
            String name, int sortOrder) {
        return new RegisterProductGroupFullCommand.OptionValueCommand(name, sortOrder);
    }

    public static RegisterProductGroupFullCommand.ProductCommand productCommand(
            int additionalPrice, int stockQuantity, int sortOrder, List<SelectedOption> options) {
        return new RegisterProductGroupFullCommand.ProductCommand(
                additionalPrice, stockQuantity, sortOrder, options);
    }

    public static RegisterProductGroupFullCommand.DescriptionCommand descriptionCommand(
            String content, String cdnPath) {
        return new RegisterProductGroupFullCommand.DescriptionCommand(content, cdnPath);
    }

    public static RegisterProductGroupFullCommand.NoticeCommand noticeCommand(
            List<RegisterProductGroupFullCommand.NoticeEntryCommand> entries) {
        return new RegisterProductGroupFullCommand.NoticeCommand(entries);
    }

    public static RegisterProductGroupFullCommand.NoticeEntryCommand noticeEntryCommand(
            String fieldName, String fieldValue, int sortOrder) {
        return new RegisterProductGroupFullCommand.NoticeEntryCommand(
                fieldName, fieldValue, sortOrder);
    }

    // ===== UpdateProductGroupFullCommand Fixtures =====

    public static UpdateProductGroupFullCommand updateFullCommand() {
        return updateFullCommand(1L);
    }

    public static UpdateProductGroupFullCommand updateFullCommand(Long productGroupId) {
        return new UpdateProductGroupFullCommand(
                productGroupId,
                1L,
                1L,
                1L,
                1L,
                "수정된 상품그룹",
                "SINGLE",
                50000,
                45000,
                "ACTIVE",
                List.of(updateImageCommand("THUMBNAIL", "https://example.com/new-thumb.png", 1)),
                List.of(updateOptionGroupCommand("색상", List.of(updateOptionValueCommand("흰색", 1)))),
                List.of(updateProductCommand(1L, 0, 20, "ACTIVE", 1, List.of())),
                updateDescriptionCommand("수정된 설명 내용", null),
                updateNoticeCommand(List.of(updateNoticeEntryCommand("소재", "폴리에스터 100%", 1))));
    }

    public static UpdateProductGroupFullCommand.ImageCommand updateImageCommand(
            String imageType, String imageUrl, int sortOrder) {
        return new UpdateProductGroupFullCommand.ImageCommand(imageType, imageUrl, sortOrder);
    }

    public static UpdateProductGroupFullCommand.OptionGroupCommand updateOptionGroupCommand(
            String name, List<UpdateProductGroupFullCommand.OptionValueCommand> values) {
        return new UpdateProductGroupFullCommand.OptionGroupCommand(name, values);
    }

    public static UpdateProductGroupFullCommand.OptionValueCommand updateOptionValueCommand(
            String name, int sortOrder) {
        return new UpdateProductGroupFullCommand.OptionValueCommand(name, sortOrder);
    }

    public static UpdateProductGroupFullCommand.ProductCommand updateProductCommand(
            Long productId,
            int additionalPrice,
            int stockQuantity,
            String status,
            int sortOrder,
            List<SelectedOption> options) {
        return new UpdateProductGroupFullCommand.ProductCommand(
                productId, additionalPrice, stockQuantity, status, sortOrder, options);
    }

    public static UpdateProductGroupFullCommand.DescriptionCommand updateDescriptionCommand(
            String content, String cdnPath) {
        return new UpdateProductGroupFullCommand.DescriptionCommand(content, cdnPath);
    }

    public static UpdateProductGroupFullCommand.NoticeCommand updateNoticeCommand(
            List<UpdateProductGroupFullCommand.NoticeEntryCommand> entries) {
        return new UpdateProductGroupFullCommand.NoticeCommand(entries);
    }

    public static UpdateProductGroupFullCommand.NoticeEntryCommand updateNoticeEntryCommand(
            String fieldName, String fieldValue, int sortOrder) {
        return new UpdateProductGroupFullCommand.NoticeEntryCommand(
                fieldName, fieldValue, sortOrder);
    }

    // ===== UpdateProductGroupBasicInfoCommand Fixtures =====

    public static UpdateProductGroupBasicInfoCommand updateBasicInfoCommand() {
        return updateBasicInfoCommand(1L);
    }

    public static UpdateProductGroupBasicInfoCommand updateBasicInfoCommand(Long productGroupId) {
        return new UpdateProductGroupBasicInfoCommand(productGroupId, "수정된 상품그룹명", 1L, 1L, 1L, 1L);
    }

    public static UpdateProductGroupBasicInfoCommand updateBasicInfoCommandWithNullOptionals(
            Long productGroupId) {
        return new UpdateProductGroupBasicInfoCommand(
                productGroupId, "수정된 상품그룹명", null, null, null, null);
    }

    // ===== Bundle Fixtures =====

    public static ProductGroupRegistrationBundle registrationBundle() {
        Instant now = Instant.now();
        return new ProductGroupRegistrationBundle(
                ProductGroupFixtures.newProductGroup(),
                List.of(
                        new RegisterProductGroupImagesCommand.ImageCommand(
                                "THUMBNAIL", "https://example.com/thumb.png", 1)),
                "SINGLE",
                List.of(
                        new RegisterSellerOptionGroupsCommand.OptionGroupCommand(
                                "색상",
                                List.of(
                                        new RegisterSellerOptionGroupsCommand.OptionValueCommand(
                                                "검정", 1)))),
                "상품 설명 내용",
                null,
                List.of(new RegisterProductNoticeCommand.NoticeEntryCommand("소재", "면 100%", 1)),
                List.of(new RegisterProductsCommand.ProductData(0, 10, 1, List.of())),
                now);
    }

    public static ProductGroupRegistrationBundle registrationBundleWithNullOptionals() {
        Instant now = Instant.now();
        return new ProductGroupRegistrationBundle(
                ProductGroupFixtures.newProductGroup(),
                List.of(),
                "NONE",
                List.of(),
                null,
                null,
                List.of(),
                List.of(),
                now);
    }

    public static ProductGroupUpdateBundle updateBundle() {
        ProductGroupUpdateData basicInfoUpdateData =
                ProductGroupUpdateData.of(
                        ProductGroupId.of(1L),
                        ProductGroupName.of("수정된 상품그룹명"),
                        BrandId.of(1L),
                        CategoryId.of(1L),
                        ShippingPolicyId.of(1L),
                        RefundPolicyId.of(1L),
                        OptionType.SINGLE,
                        Instant.now());

        UpdateProductGroupImagesCommand imageCommand =
                new UpdateProductGroupImagesCommand(
                        1L,
                        List.of(
                                new UpdateProductGroupImagesCommand.ImageCommand(
                                        "THUMBNAIL", "https://example.com/new-thumb.png", 1)));

        UpdateSellerOptionGroupsCommand optionGroupCommand =
                new UpdateSellerOptionGroupsCommand(
                        1L,
                        List.of(
                                new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                                        null,
                                        "색상",
                                        null,
                                        null,
                                        List.of(
                                                new UpdateSellerOptionGroupsCommand
                                                        .OptionValueCommand(
                                                        null, "흰색", null, 1)))));

        UpdateProductGroupDescriptionCommand descriptionCommand =
                new UpdateProductGroupDescriptionCommand(1L, "수정된 설명 내용", null);

        UpdateProductNoticeCommand noticeCommand =
                new UpdateProductNoticeCommand(
                        1L,
                        List.of(
                                new UpdateProductNoticeCommand.NoticeEntryCommand(
                                        "소재", "폴리에스터 100%", 1)));

        List<ProductDiffUpdateEntry> productEntries =
                List.of(new ProductDiffUpdateEntry(1L, null, 0, 0, 20, 1, List.of()));

        return new ProductGroupUpdateBundle(
                basicInfoUpdateData,
                imageCommand,
                optionGroupCommand,
                descriptionCommand,
                noticeCommand,
                productEntries);
    }

    // ===== ProductGroupUpdateData Fixture =====

    public static ProductGroupUpdateData productGroupUpdateData() {
        return productGroupUpdateData(1L);
    }

    public static ProductGroupUpdateData productGroupUpdateData(Long productGroupId) {
        return ProductGroupUpdateData.of(
                ProductGroupId.of(productGroupId),
                ProductGroupName.of("수정된 상품그룹명"),
                BrandId.of(1L),
                CategoryId.of(1L),
                ShippingPolicyId.of(1L),
                RefundPolicyId.of(1L),
                OptionType.SINGLE,
                Instant.now());
    }
}

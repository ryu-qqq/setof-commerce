package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.internal.ProductCommandCoordinator;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.internal.DescriptionCommandCoordinator;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCommandManager;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
import com.ryuqq.setof.application.productnotice.manager.ProductNoticeCommandManager;
import com.ryuqq.setof.application.productnotice.manager.ProductNoticeEntryCommandManager;
import com.ryuqq.setof.application.productnotice.manager.ProductNoticeReadManager;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionCommandCoordinator;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionPersistFacade;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductCreationData;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import com.ryuqq.setof.domain.productnotice.id.NoticeFieldId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

/**
 * ProductGroupPersistFacade - 상품그룹 등록/수정의 하위 도메인 영속화를 조율하는 Facade.
 *
 * <p>이미지, 옵션, 상세설명, 고시, 상품의 영속화를 각 전용 매니저/코디네이터에 위임합니다.
 */
@Component
public class ProductGroupPersistFacade {

    private final ProductGroupCommandManager productGroupCommandManager;
    private final ImageCommandCoordinator imageCommandCoordinator;
    private final SellerOptionPersistFacade sellerOptionPersistFacade;
    private final SellerOptionCommandCoordinator sellerOptionCommandCoordinator;
    private final DescriptionCommandCoordinator descriptionCommandCoordinator;
    private final ProductNoticeCommandManager noticeCommandManager;
    private final ProductNoticeEntryCommandManager noticeEntryCommandManager;
    private final ProductNoticeReadManager noticeReadManager;
    private final ProductCommandCoordinator productCommandCoordinator;
    private final ProductCommandFactory productCommandFactory;

    public ProductGroupPersistFacade(
            ProductGroupCommandManager productGroupCommandManager,
            ImageCommandCoordinator imageCommandCoordinator,
            SellerOptionPersistFacade sellerOptionPersistFacade,
            SellerOptionCommandCoordinator sellerOptionCommandCoordinator,
            DescriptionCommandCoordinator descriptionCommandCoordinator,
            ProductNoticeCommandManager noticeCommandManager,
            ProductNoticeEntryCommandManager noticeEntryCommandManager,
            ProductNoticeReadManager noticeReadManager,
            ProductCommandCoordinator productCommandCoordinator,
            ProductCommandFactory productCommandFactory) {
        this.productGroupCommandManager = productGroupCommandManager;
        this.imageCommandCoordinator = imageCommandCoordinator;
        this.sellerOptionPersistFacade = sellerOptionPersistFacade;
        this.sellerOptionCommandCoordinator = sellerOptionCommandCoordinator;
        this.descriptionCommandCoordinator = descriptionCommandCoordinator;
        this.noticeCommandManager = noticeCommandManager;
        this.noticeEntryCommandManager = noticeEntryCommandManager;
        this.noticeReadManager = noticeReadManager;
        this.productCommandCoordinator = productCommandCoordinator;
        this.productCommandFactory = productCommandFactory;
    }

    /**
     * 상품그룹 등록 시 하위 도메인 전체를 영속화합니다.
     *
     * @param productGroup 상품그룹 도메인 객체
     * @param command 등록 커맨드
     * @param now 생성 시각
     * @return 저장된 상품그룹 ID
     */
    public Long registerAll(
            ProductGroup productGroup, RegisterProductGroupCommand command, Instant now) {

        Long productGroupId = productGroupCommandManager.persist(productGroup);

        registerImages(productGroupId, command);
        List<SellerOptionValueId> optionValueIds =
                registerOptionGroups(ProductGroupId.of(productGroupId), command);
        registerDescription(productGroupId, command);
        registerNotice(ProductGroupId.of(productGroupId), command, now);
        registerProducts(productGroupId, command, optionValueIds, now);

        return productGroupId;
    }

    /**
     * 상품그룹 수정 시 하위 도메인 전체를 갱신합니다.
     *
     * @param productGroup 기존 상품그룹 도메인 객체
     * @param command 수정 커맨드
     * @param now 수정 시각
     */
    public void updateAll(
            ProductGroup productGroup, UpdateProductGroupFullCommand command, Instant now) {

        ProductGroupId productGroupId = productGroup.id();
        Long productGroupIdValue = productGroupId.value();

        updateBasicInfo(productGroup, command, now);
        productGroupCommandManager.persist(productGroup);

        updateImages(productGroupIdValue, command);
        SellerOptionUpdateResult optionResult = updateOptionGroups(productGroupId, command);
        updateDescription(productGroupIdValue, command);
        updateNotice(productGroupId, command, now);
        updateProducts(productGroupId, command, optionResult);
    }

    // ── 등록 내부 메서드 ──

    private void registerImages(Long productGroupId, RegisterProductGroupCommand command) {
        if (command.images() == null || command.images().isEmpty()) {
            return;
        }
        List<RegisterProductGroupImagesCommand.ImageCommand> imageCommands =
                command.images().stream()
                        .map(
                                img ->
                                        new RegisterProductGroupImagesCommand.ImageCommand(
                                                img.imageType(), img.imageUrl(), img.sortOrder()))
                        .toList();
        imageCommandCoordinator.register(
                new RegisterProductGroupImagesCommand(productGroupId, imageCommands));
    }

    private List<SellerOptionValueId> registerOptionGroups(
            ProductGroupId productGroupId, RegisterProductGroupCommand command) {

        if (command.optionGroups() == null || command.optionGroups().isEmpty()) {
            return List.of();
        }

        List<SellerOptionGroup> groups =
                command.optionGroups().stream()
                        .map(
                                og -> {
                                    List<SellerOptionValue> values =
                                            og.optionValues().stream()
                                                    .map(
                                                            ov ->
                                                                    SellerOptionValue.forNew(
                                                                            SellerOptionGroupId
                                                                                    .forNew(),
                                                                            OptionValueName.of(
                                                                                    ov
                                                                                            .optionValueName()),
                                                                            ov.sortOrder()))
                                                    .toList();
                                    return SellerOptionGroup.forNew(
                                            productGroupId,
                                            OptionGroupName.of(og.optionGroupName()),
                                            og.sortOrder(),
                                            values);
                                })
                        .toList();

        return sellerOptionPersistFacade.persistAll(groups);
    }

    private void registerDescription(Long productGroupId, RegisterProductGroupCommand command) {
        if (command.description() == null) {
            return;
        }
        RegisterProductGroupCommand.DescriptionCommand descCmd = command.description();
        List<RegisterProductGroupDescriptionCommand.DescriptionImageCommand> descImages =
                descCmd.descriptionImages() != null
                        ? descCmd.descriptionImages().stream()
                                .map(
                                        di ->
                                                new RegisterProductGroupDescriptionCommand
                                                        .DescriptionImageCommand(
                                                        di.imageUrl(), di.sortOrder()))
                                .toList()
                        : List.of();

        descriptionCommandCoordinator.register(
                new RegisterProductGroupDescriptionCommand(
                        productGroupId, descCmd.content(), descImages));
    }

    private void registerNotice(
            ProductGroupId productGroupId, RegisterProductGroupCommand command, Instant now) {
        if (command.notice() == null
                || command.notice().entries() == null
                || command.notice().entries().isEmpty()) {
            return;
        }

        List<ProductNoticeEntry> entries =
                IntStream.range(0, command.notice().entries().size())
                        .mapToObj(
                                i -> {
                                    RegisterProductGroupCommand.NoticeEntryCommand entryCmd =
                                            command.notice().entries().get(i);
                                    return ProductNoticeEntry.forNew(
                                            NoticeFieldId.of(entryCmd.noticeFieldId()),
                                            NoticeFieldValue.of(
                                                    entryCmd.fieldName(), entryCmd.fieldValue()),
                                            i);
                                })
                        .toList();

        ProductNotice notice = ProductNotice.forNew(productGroupId, entries, now);
        Long noticeId = noticeCommandManager.persist(notice);
        notice.assignId(ProductNoticeId.of(noticeId));
        noticeEntryCommandManager.persistAll(notice.entries());
    }

    private void registerProducts(
            Long productGroupId,
            RegisterProductGroupCommand command,
            List<SellerOptionValueId> allOptionValueIds,
            Instant now) {

        if (command.products() == null || command.products().isEmpty()) {
            return;
        }

        List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroupCommands =
                toOptionGroupCommands(command.optionGroups());

        List<com.ryuqq.setof.application.product.dto.command.RegisterProductsCommand.ProductData>
                productDataList =
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
                                .toList();

        List<ProductCreationData> creationDataList =
                productCommandFactory.toCreationDataList(
                        productDataList, optionGroupCommands, allOptionValueIds);

        List<Product> products =
                creationDataList.stream()
                        .map(data -> data.toProduct(ProductGroupId.of(productGroupId), now))
                        .toList();

        productCommandCoordinator.register(products);
    }

    // ── 수정 내부 메서드 ──

    private void updateBasicInfo(
            ProductGroup productGroup, UpdateProductGroupFullCommand command, Instant now) {
        com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData updateData =
                com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData.of(
                        productGroup.id(),
                        ProductGroupName.of(command.productGroupName()),
                        com.ryuqq.setof.domain.brand.id.BrandId.of(command.brandId()),
                        com.ryuqq.setof.domain.category.id.CategoryId.of(command.categoryId()),
                        com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId.of(
                                command.shippingPolicyId()),
                        com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId.of(
                                command.refundPolicyId()),
                        OptionType.valueOf(command.optionType()),
                        now);
        productGroup.update(updateData);
        productGroup.updatePrices(
                com.ryuqq.setof.domain.common.vo.Money.of(command.regularPrice()),
                com.ryuqq.setof.domain.common.vo.Money.of(command.currentPrice()),
                com.ryuqq.setof.domain.common.vo.Money.of(command.currentPrice()),
                now);
    }

    private void updateImages(Long productGroupId, UpdateProductGroupFullCommand command) {
        if (command.images() == null || command.images().isEmpty()) {
            return;
        }
        List<UpdateProductGroupImagesCommand.ImageCommand> imageCommands =
                command.images().stream()
                        .map(
                                img ->
                                        new UpdateProductGroupImagesCommand.ImageCommand(
                                                img.imageType(), img.imageUrl(), img.sortOrder()))
                        .toList();
        imageCommandCoordinator.update(
                new UpdateProductGroupImagesCommand(productGroupId, imageCommands));
    }

    private SellerOptionUpdateResult updateOptionGroups(
            ProductGroupId productGroupId, UpdateProductGroupFullCommand command) {

        if (command.optionGroups() == null || command.optionGroups().isEmpty()) {
            return new SellerOptionUpdateResult(List.of(), Instant.now(), false);
        }

        List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroupCommands =
                command.optionGroups().stream()
                        .map(
                                og -> {
                                    List<UpdateSellerOptionGroupsCommand.OptionValueCommand>
                                            valueCommands =
                                                    og.optionValues().stream()
                                                            .map(
                                                                    ov ->
                                                                            new UpdateSellerOptionGroupsCommand
                                                                                    .OptionValueCommand(
                                                                                    ov
                                                                                            .sellerOptionValueId(),
                                                                                    ov
                                                                                            .optionValueName(),
                                                                                    ov.sortOrder()))
                                                            .toList();
                                    return new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                                            og.sellerOptionGroupId(),
                                            og.optionGroupName(),
                                            og.sortOrder(),
                                            valueCommands);
                                })
                        .toList();

        UpdateSellerOptionGroupsCommand optionCmd =
                new UpdateSellerOptionGroupsCommand(productGroupId.value(), optionGroupCommands);
        return sellerOptionCommandCoordinator.update(optionCmd);
    }

    private void updateDescription(Long productGroupId, UpdateProductGroupFullCommand command) {
        if (command.description() == null) {
            return;
        }
        UpdateProductGroupFullCommand.DescriptionCommand descCmd = command.description();
        List<UpdateProductGroupDescriptionCommand.DescriptionImageCommand> descImages =
                descCmd.descriptionImages() != null
                        ? descCmd.descriptionImages().stream()
                                .map(
                                        di ->
                                                new UpdateProductGroupDescriptionCommand
                                                        .DescriptionImageCommand(
                                                        di.imageUrl(), di.sortOrder()))
                                .toList()
                        : List.of();

        descriptionCommandCoordinator.update(
                new UpdateProductGroupDescriptionCommand(
                        productGroupId, descCmd.content(), descImages));
    }

    private void updateNotice(
            ProductGroupId productGroupId, UpdateProductGroupFullCommand command, Instant now) {
        if (command.notice() == null
                || command.notice().entries() == null
                || command.notice().entries().isEmpty()) {
            return;
        }

        ProductNotice notice = noticeReadManager.getByProductGroupId(productGroupId);
        List<ProductNoticeEntry> newEntries =
                IntStream.range(0, command.notice().entries().size())
                        .mapToObj(
                                i -> {
                                    UpdateProductGroupFullCommand.NoticeEntryCommand entryCmd =
                                            command.notice().entries().get(i);
                                    return ProductNoticeEntry.forNew(
                                            NoticeFieldId.of(entryCmd.noticeFieldId()),
                                            NoticeFieldValue.of(
                                                    entryCmd.fieldName(), entryCmd.fieldValue()),
                                            i);
                                })
                        .toList();

        com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData noticeUpdateData =
                com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData.of(newEntries, now);
        notice.update(noticeUpdateData);

        noticeEntryCommandManager.deleteByProductNoticeId(notice.idValue());
        noticeCommandManager.persist(notice);
        noticeEntryCommandManager.persistAll(notice.entries());
    }

    private void updateProducts(
            ProductGroupId productGroupId,
            UpdateProductGroupFullCommand command,
            SellerOptionUpdateResult optionResult) {

        if (command.products() == null || command.products().isEmpty()) {
            return;
        }

        List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroupCommands =
                command.optionGroups() != null
                        ? command.optionGroups().stream()
                                .map(
                                        og -> {
                                            List<UpdateSellerOptionGroupsCommand.OptionValueCommand>
                                                    valueCommands =
                                                            og.optionValues().stream()
                                                                    .map(
                                                                            ov ->
                                                                                    new UpdateSellerOptionGroupsCommand
                                                                                            .OptionValueCommand(
                                                                                            ov
                                                                                                    .sellerOptionValueId(),
                                                                                            ov
                                                                                                    .optionValueName(),
                                                                                            ov
                                                                                                    .sortOrder()))
                                                                    .toList();
                                            return new UpdateSellerOptionGroupsCommand
                                                    .OptionGroupCommand(
                                                    og.sellerOptionGroupId(),
                                                    og.optionGroupName(),
                                                    og.sortOrder(),
                                                    valueCommands);
                                        })
                                .toList()
                        : List.of();

        List<ProductDiffUpdateEntry> entries =
                command.products().stream()
                        .map(
                                p ->
                                        new ProductDiffUpdateEntry(
                                                p.productId(),
                                                p.skuCode(),
                                                p.regularPrice(),
                                                p.currentPrice(),
                                                p.stockQuantity(),
                                                p.sortOrder(),
                                                p.selectedOptions()))
                        .toList();

        ProductUpdateData updateData =
                productCommandFactory.toUpdateData(
                        productGroupId,
                        entries,
                        optionGroupCommands,
                        optionResult.resolvedActiveValueIds(),
                        optionResult.occurredAt());

        productCommandCoordinator.update(productGroupId, updateData);
    }

    // ── 유틸 ──

    private List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> toOptionGroupCommands(
            List<RegisterProductGroupCommand.OptionGroupCommand> optionGroups) {
        if (optionGroups == null || optionGroups.isEmpty()) {
            return List.of();
        }
        return optionGroups.stream()
                .map(
                        og ->
                                new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                                        null,
                                        og.optionGroupName(),
                                        og.sortOrder(),
                                        og.optionValues().stream()
                                                .map(
                                                        ov ->
                                                                new UpdateSellerOptionGroupsCommand
                                                                        .OptionValueCommand(
                                                                        null,
                                                                        ov.optionValueName(),
                                                                        ov.sortOrder()))
                                                .toList()))
                .toList();
    }
}

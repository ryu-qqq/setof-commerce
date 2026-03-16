package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.internal.DescriptionCommandCoordinator;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCommandManager;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupPriceCommandManager;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionCommandCoordinator;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductCreationData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FullProductGroupRegistrationCoordinator - 상품그룹 전체 등록을 조율하는 Coordinator.
 *
 * <p>Bundle로 전달된 per-package Command를 각 전용 Coordinator/Manager에 위임하여 상품그룹 + 이미지 + 옵션 + 상세설명 + 고시 +
 * 상품을 한 트랜잭션 내에서 등록합니다.
 */
@Component
public class FullProductGroupRegistrationCoordinator {

    private final ProductGroupCommandManager productGroupCommandManager;
    private final ProductGroupPriceCommandManager priceCommandManager;
    private final ImageCommandCoordinator imageCommandCoordinator;
    private final SellerOptionCommandCoordinator sellerOptionCommandCoordinator;
    private final DescriptionCommandCoordinator descriptionCommandCoordinator;
    private final ProductNoticeCommandCoordinator noticeCommandCoordinator;
    private final com.ryuqq.setof.application.product.internal.ProductCommandCoordinator
            productCommandCoordinator;
    private final ProductCommandFactory productCommandFactory;

    public FullProductGroupRegistrationCoordinator(
            ProductGroupCommandManager productGroupCommandManager,
            ProductGroupPriceCommandManager priceCommandManager,
            ImageCommandCoordinator imageCommandCoordinator,
            SellerOptionCommandCoordinator sellerOptionCommandCoordinator,
            DescriptionCommandCoordinator descriptionCommandCoordinator,
            ProductNoticeCommandCoordinator noticeCommandCoordinator,
            com.ryuqq.setof.application.product.internal.ProductCommandCoordinator
                    productCommandCoordinator,
            ProductCommandFactory productCommandFactory) {
        this.productGroupCommandManager = productGroupCommandManager;
        this.priceCommandManager = priceCommandManager;
        this.imageCommandCoordinator = imageCommandCoordinator;
        this.sellerOptionCommandCoordinator = sellerOptionCommandCoordinator;
        this.descriptionCommandCoordinator = descriptionCommandCoordinator;
        this.noticeCommandCoordinator = noticeCommandCoordinator;
        this.productCommandCoordinator = productCommandCoordinator;
        this.productCommandFactory = productCommandFactory;
    }

    /**
     * 상품그룹 전체 등록을 실행합니다.
     *
     * @param bundle 등록 번들 (도메인 객체 + per-package Command + 생성 시각)
     * @return 저장된 상품그룹 ID
     */
    @Transactional
    public Long register(ProductGroupRegistrationBundle bundle) {
        // 1. 상품그룹 기본 정보 저장
        Long productGroupId = productGroupCommandManager.persist(bundle.productGroup());

        // 2. 가격 초기화
        priceCommandManager.persist(productGroupId);

        // 3. 이미지 등록
        if (!bundle.images().isEmpty()) {
            imageCommandCoordinator.register(
                    new RegisterProductGroupImagesCommand(productGroupId, bundle.images()));
        }

        // 4. 옵션 그룹 등록
        List<SellerOptionValueId> allOptionValueIds =
                sellerOptionCommandCoordinator.register(
                        ProductGroupId.of(productGroupId), bundle.optionGroups());

        // 5. 상세설명 등록
        if (bundle.descriptionContent() != null) {
            List<RegisterProductGroupDescriptionCommand.DescriptionImageCommand> descImages =
                    toDescriptionImageCommands(bundle.descriptionImages());
            descriptionCommandCoordinator.register(
                    new RegisterProductGroupDescriptionCommand(
                            productGroupId, bundle.descriptionContent(), descImages));
        }

        // 6. 상품고시 등록
        if (!bundle.noticeEntries().isEmpty()) {
            noticeCommandCoordinator.register(
                    new RegisterProductNoticeCommand(productGroupId, bundle.noticeEntries()));
        }

        // 7. 상품 등록
        if (!bundle.products().isEmpty()) {
            registerProducts(productGroupId, bundle, allOptionValueIds);
        }

        return productGroupId;
    }

    private void registerProducts(
            Long productGroupId,
            ProductGroupRegistrationBundle bundle,
            List<SellerOptionValueId> allOptionValueIds) {

        List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroupCommands =
                toUpdateOptionGroupCommands(bundle.optionGroups());

        List<ProductCreationData> creationDataList =
                productCommandFactory.toCreationDataList(
                        bundle.products(), optionGroupCommands, allOptionValueIds);

        List<Product> products =
                creationDataList.stream()
                        .map(
                                data ->
                                        data.toProduct(
                                                ProductGroupId.of(productGroupId),
                                                bundle.createdAt()))
                        .toList();

        productCommandCoordinator.register(products);
    }

    private static List<UpdateSellerOptionGroupsCommand.OptionGroupCommand>
            toUpdateOptionGroupCommands(
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

    private static List<RegisterProductGroupDescriptionCommand.DescriptionImageCommand>
            toDescriptionImageCommands(
                    List<RegisterProductGroupCommand.DescriptionImageCommand> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream()
                .map(
                        di ->
                                new RegisterProductGroupDescriptionCommand.DescriptionImageCommand(
                                        di.imageUrl(), di.sortOrder()))
                .toList();
    }
}

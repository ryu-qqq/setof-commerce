package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.internal.ProductCommandCoordinator;
import com.ryuqq.setof.application.productdescription.internal.DescriptionCommandCoordinator;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCommandManager;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionCommandCoordinator;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FullProductGroupUpdateCoordinator - 상품그룹 전체 수정을 조율하는 Coordinator.
 *
 * <p>Bundle에 미리 준비된 per-package Update Command를 각 전용 Coordinator/Manager에 위임하여 상품그룹 + 이미지 + 옵션 +
 * 상세설명 + 고시 + 상품을 한 트랜잭션 내에서 수정합니다.
 */
@Component
public class FullProductGroupUpdateCoordinator {

    private final ProductGroupCommandManager productGroupCommandManager;
    private final ImageCommandCoordinator imageCommandCoordinator;
    private final SellerOptionCommandCoordinator sellerOptionCommandCoordinator;
    private final DescriptionCommandCoordinator descriptionCommandCoordinator;
    private final ProductNoticeCommandCoordinator noticeCommandCoordinator;
    private final ProductCommandCoordinator productCommandCoordinator;
    private final ProductCommandFactory productCommandFactory;

    public FullProductGroupUpdateCoordinator(
            ProductGroupCommandManager productGroupCommandManager,
            ImageCommandCoordinator imageCommandCoordinator,
            SellerOptionCommandCoordinator sellerOptionCommandCoordinator,
            DescriptionCommandCoordinator descriptionCommandCoordinator,
            ProductNoticeCommandCoordinator noticeCommandCoordinator,
            ProductCommandCoordinator productCommandCoordinator,
            ProductCommandFactory productCommandFactory) {
        this.productGroupCommandManager = productGroupCommandManager;
        this.imageCommandCoordinator = imageCommandCoordinator;
        this.sellerOptionCommandCoordinator = sellerOptionCommandCoordinator;
        this.descriptionCommandCoordinator = descriptionCommandCoordinator;
        this.noticeCommandCoordinator = noticeCommandCoordinator;
        this.productCommandCoordinator = productCommandCoordinator;
        this.productCommandFactory = productCommandFactory;
    }

    /**
     * 상품그룹 전체 수정을 실행합니다.
     *
     * @param bundle 수정 번들 (도메인 객체 + per-package Command + 수정 시각)
     */
    @Transactional
    public void update(ProductGroupUpdateBundle bundle) {
        ProductGroup productGroup = bundle.productGroup();
        ProductGroupId productGroupId = productGroup.id();

        // 1. 기본 정보 + 가격 수정 (Bundle에서 이미 domain update 적용됨)
        productGroupCommandManager.persist(productGroup);

        // 2. 이미지 수정
        if (bundle.imageCommand() != null) {
            imageCommandCoordinator.update(bundle.imageCommand());
        }

        // 3. 옵션 수정
        SellerOptionUpdateResult optionResult = updateOptionGroups(bundle);

        // 4. 상세설명 수정
        if (bundle.descriptionCommand() != null) {
            descriptionCommandCoordinator.update(bundle.descriptionCommand());
        }

        // 5. 상품고시 수정
        if (bundle.noticeCommand() != null) {
            noticeCommandCoordinator.update(bundle.noticeCommand());
        }

        // 6. 상품 수정
        if (!bundle.productEntries().isEmpty()) {
            updateProducts(productGroupId, bundle, optionResult);
        }
    }

    private SellerOptionUpdateResult updateOptionGroups(ProductGroupUpdateBundle bundle) {
        if (bundle.optionGroupCommand() == null) {
            return new SellerOptionUpdateResult(List.of(), Instant.now(), false);
        }
        return sellerOptionCommandCoordinator.update(bundle.optionGroupCommand());
    }

    private void updateProducts(
            ProductGroupId productGroupId,
            ProductGroupUpdateBundle bundle,
            SellerOptionUpdateResult optionResult) {

        ProductUpdateData updateData =
                productCommandFactory.toUpdateData(
                        productGroupId,
                        bundle.productEntries(),
                        bundle.optionGroupCommand() != null
                                ? bundle.optionGroupCommand().optionGroups()
                                : List.of(),
                        optionResult.resolvedActiveValueIds(),
                        optionResult.occurredAt());

        productCommandCoordinator.update(productGroupId, updateData);
    }
}

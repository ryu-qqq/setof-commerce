package com.ryuqq.setof.application.product.facade;

import com.ryuqq.setof.application.product.component.ExistingProductData;
import com.ryuqq.setof.application.product.component.ProductChangeDetector;
import com.ryuqq.setof.application.product.component.ProductChanges;
import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.product.manager.command.ProductGroupPersistenceManager;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.productdescription.manager.command.ProductDescriptionPersistenceManager;
import com.ryuqq.setof.application.productdescription.manager.query.ProductDescriptionReadManager;
import com.ryuqq.setof.application.productimage.dto.command.RegisterProductImageCommand;
import com.ryuqq.setof.application.productimage.factory.command.ProductImageCommandFactory;
import com.ryuqq.setof.application.productimage.manager.command.ProductImageWriteManager;
import com.ryuqq.setof.application.productimage.manager.query.ProductImageReadManager;
import com.ryuqq.setof.application.productnotice.manager.command.ProductNoticePersistenceManager;
import com.ryuqq.setof.application.productnotice.manager.query.ProductNoticeReadManager;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품 수정 Facade
 *
 * <p>상품그룹 + 이미지 + 설명 + 고시 변경 감지 후 업데이트를 조율합니다.
 *
 * <p>트랜잭션 없음 - Service에서 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductModificationFacade {

    private final ProductChangeDetector changeDetector;
    private final ProductImageCommandFactory imageFactory;

    // Write Managers
    private final ProductGroupPersistenceManager productGroupManager;
    private final ProductImageWriteManager imageManager;
    private final ProductDescriptionPersistenceManager descriptionManager;
    private final ProductNoticePersistenceManager noticeManager;

    // Read Managers (기존 데이터 로드용)
    private final ProductGroupReadManager productGroupReadManager;
    private final ProductImageReadManager imageReadManager;
    private final ProductDescriptionReadManager descriptionReadManager;
    private final ProductNoticeReadManager noticeReadManager;

    public ProductModificationFacade(
            ProductChangeDetector changeDetector,
            ProductImageCommandFactory imageFactory,
            ProductGroupPersistenceManager productGroupManager,
            ProductImageWriteManager imageManager,
            ProductDescriptionPersistenceManager descriptionManager,
            ProductNoticePersistenceManager noticeManager,
            ProductGroupReadManager productGroupReadManager,
            ProductImageReadManager imageReadManager,
            ProductDescriptionReadManager descriptionReadManager,
            ProductNoticeReadManager noticeReadManager) {
        this.changeDetector = changeDetector;
        this.imageFactory = imageFactory;
        this.productGroupManager = productGroupManager;
        this.imageManager = imageManager;
        this.descriptionManager = descriptionManager;
        this.noticeManager = noticeManager;
        this.productGroupReadManager = productGroupReadManager;
        this.imageReadManager = imageReadManager;
        this.descriptionReadManager = descriptionReadManager;
        this.noticeReadManager = noticeReadManager;
    }

    /**
     * 전체 상품 수정 (Diff 비교 후 변경분만 업데이트)
     *
     * @param command 전체 수정 Command
     */
    public void updateAll(UpdateFullProductCommand command) {
        Long productGroupId = command.productGroupId();

        // 1. 기존 데이터 조회
        ExistingProductData existing = loadExistingData(productGroupId);

        // 2. 변경 감지
        ProductChanges changes = changeDetector.detect(existing, command);

        // 3. ProductGroup 변경 처리
        if (changes.hasProductGroupChanges()) {
            productGroupManager.persist(changes.updatedProductGroup());
        }

        // 4. Images 변경 처리 (추가/수정/삭제)
        if (changes.hasImageChanges()) {
            processImageChanges(productGroupId, changes);
        }

        // 5. Description 변경 처리
        if (changes.hasDescriptionChanges()) {
            descriptionManager.update(changes.updatedDescription());
        }

        // 6. Notice 변경 처리
        if (changes.hasNoticeChanges()) {
            noticeManager.update(changes.updatedNotice());
        }
    }

    /** 기존 데이터 로드 */
    private ExistingProductData loadExistingData(Long productGroupId) {
        ProductGroup productGroup = productGroupReadManager.findById(productGroupId);
        List<ProductImage> images = imageReadManager.findByProductGroupId(productGroupId);
        ProductDescription description =
                descriptionReadManager.findByProductGroupId(productGroupId).orElse(null);
        ProductNotice notice = noticeReadManager.findByProductGroupId(productGroupId).orElse(null);

        return new ExistingProductData(productGroup, images, description, notice);
    }

    /** 이미지 변경 처리 */
    private void processImageChanges(Long productGroupId, ProductChanges changes) {
        // 삭제
        for (Long imageId : changes.imageIdsToDelete()) {
            imageManager.delete(imageId);
        }

        // 수정
        for (ProductImage image : changes.imagesToUpdate()) {
            imageManager.update(image);
        }

        // 추가
        for (ProductImageCommandDto dto : changes.imageDtosToAdd()) {
            RegisterProductImageCommand imageCommand =
                    new RegisterProductImageCommand(
                            productGroupId,
                            dto.imageType(),
                            dto.originUrl(),
                            dto.cdnUrl(),
                            dto.displayOrder());
            ProductImage productImage = imageFactory.createFromRegisterCommand(imageCommand);
            imageManager.save(productImage);
        }
    }
}

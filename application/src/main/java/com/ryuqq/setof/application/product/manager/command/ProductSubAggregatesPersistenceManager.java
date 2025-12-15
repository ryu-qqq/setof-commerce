package com.ryuqq.setof.application.product.manager.command;

import com.ryuqq.setof.application.product.dto.bundle.ProductSubAggregatesPersistBundle;
import com.ryuqq.setof.application.productdescription.manager.command.ProductDescriptionPersistenceManager;
import com.ryuqq.setof.application.productimage.manager.command.ProductImageWriteManager;
import com.ryuqq.setof.application.productnotice.manager.command.ProductNoticePersistenceManager;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Product 하위 Aggregate Bundle 영속성 Manager
 *
 * <p>Bundle 단위로 하위 Aggregate들을 일괄 저장합니다.
 *
 * <p>트랜잭션 없음 - Service에서 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductSubAggregatesPersistenceManager {

    private final ProductWriteManager productManager;
    private final ProductImageWriteManager imageManager;
    private final ProductDescriptionPersistenceManager descriptionManager;
    private final ProductNoticePersistenceManager noticeManager;

    public ProductSubAggregatesPersistenceManager(
            ProductWriteManager productManager,
            ProductImageWriteManager imageManager,
            ProductDescriptionPersistenceManager descriptionManager,
            ProductNoticePersistenceManager noticeManager) {
        this.productManager = productManager;
        this.imageManager = imageManager;
        this.descriptionManager = descriptionManager;
        this.noticeManager = noticeManager;
    }

    /**
     * Bundle 일괄 저장
     *
     * @param bundle 하위 Aggregate Bundle
     * @return 저장된 Product ID 목록 (Stock 초기화용)
     */
    public List<ProductId> persist(ProductSubAggregatesPersistBundle bundle) {
        List<ProductId> productIds = persistProducts(bundle);
        persistImages(bundle);
        persistDescription(bundle);
        persistNotice(bundle);
        return productIds;
    }

    private List<ProductId> persistProducts(ProductSubAggregatesPersistBundle bundle) {
        return productManager.saveAll(bundle.products());
    }

    private void persistImages(ProductSubAggregatesPersistBundle bundle) {
        if (!bundle.hasImages()) {
            return;
        }

        for (ProductImage image : bundle.images()) {
            imageManager.save(image);
        }
    }

    private void persistDescription(ProductSubAggregatesPersistBundle bundle) {
        descriptionManager.persist(bundle.description());
    }

    private void persistNotice(ProductSubAggregatesPersistBundle bundle) {
        noticeManager.persist(bundle.notice());
    }
}

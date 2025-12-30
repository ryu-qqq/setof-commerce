package com.connectly.partnerAdmin.module.product.service.group;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOptionDetail;
import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.dto.query.DeleteProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateCategory;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateDisplayYn;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductDescription;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductGroupDetails;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;
import com.connectly.partnerAdmin.module.product.repository.group.ProductGroupRepository;
import com.connectly.partnerAdmin.module.product.request.UpdatePriceContext;
import com.connectly.partnerAdmin.module.product.service.delivery.ProductDeliveryUpdateService;
import com.connectly.partnerAdmin.module.product.service.image.ProductImageUpdateService;
import com.connectly.partnerAdmin.module.product.service.notice.ProductNoticeUpdateService;
import com.connectly.partnerAdmin.module.product.service.stock.ProductUpdateService;
import com.connectly.partnerAdmin.module.product.service.sync.ExternalMallSyncService;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductGroupUpdateServiceImpl implements ProductGroupUpdateService {

    private final ProductGroupRepository productGroupRepository;
    private final ProductGroupFetchService productGroupFetchService;
    private final ProductNoticeUpdateService productNoticeUpdateService;
    private final ProductDeliveryUpdateService productDeliveryUpdateService;
    private final ProductImageUpdateService productImageUpdateService;
    private final ProductUpdateService productUpdateService;
    private final ExternalMallSyncService externalMallSyncService;

    @Override
    public long updateProductNotice(long productGroupId, CreateProductNotice createProductNotice) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        productNoticeUpdateService.updateProductNotice(productGroup, createProductNotice);
        return productGroup.getId();
    }

    @Override
    public long updateProductDeliveryNotice(long productGroupId, CreateDeliveryNotice deliveryNotice) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        productDeliveryUpdateService.updateProductDeliveryNotice(productGroup, deliveryNotice);
        return productGroup.getId();
    }

    @Override
    public long updateProductRefundNotice(long productGroupId, CreateRefundNotice refundNotice){
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        productDeliveryUpdateService.updateProductRefundNotice(productGroup, refundNotice);
        return productGroup.getId();
    }

    @Override
    public long updateProductImage(long productGroupId, List<CreateProductImage> createProductImages) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        productImageUpdateService.updateProductImages(productGroup, createProductImages);

        return productGroup.getId();
    }

    @Override
    public long updateDetailDescription(long productGroupId, UpdateProductDescription updateProductDescription) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        productImageUpdateService.updateProductDetailDescription(productGroup, updateProductDescription);
        return productGroup.getId();
    }

    @Override
    public long updateProductGroupCategory(long productGroupId, UpdateCategory updateCategory) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        productGroup.updateCategory(updateCategory.getCategoryId());
        return productGroupId;
    }

    @Override
    public long updatePrice(long productGroupId, CreatePrice createPrice) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        Money regularPrice = createPrice.getRegularPrice();
        Money currentPrice = createPrice.getCurrentPrice();
        productGroup.updatePrice(new Price(regularPrice, currentPrice));
        return productGroupId;
    }

    @Transactional
    @Override
    public int updatePrice(UpdatePriceContext updatePriceContext) {
        updatePriceContext.priceCommands()
            .forEach(
                p -> updatePrice(p.productGroupId(), new CreatePrice(
                    Money.wons(p.regularPrice()),
                    Money.wons(p.currentPrice())
                ))
        );

        return updatePriceContext.priceCommands().size();
    }




    @Override
    public long updateGroupDisplayYn(long productGroupId, UpdateDisplayYn updateDisplayYn) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
        productGroup.updateDisplayYn(updateDisplayYn.getDisplayYn());
        return productGroup.getId();

    }

    @Override
    public List<Long> deleteProductGroups(DeleteProductGroup deleteProductGroup) {
        deleteProductGroup.getProductGroupIds().forEach(productGroupId -> {
            ProductGroup productGroup = fetchProductGroupEntity(productGroupId);
            productGroupRepository.delete(productGroup);
        });
        return deleteProductGroup.getProductGroupIds();
    }

    @Transactional
    @Override
    public long updateProductGroup(long productGroupId, UpdateProductGroup updateProductGroup) {
        ProductGroup productGroup = fetchProductGroupEntity(productGroupId);

        UpdateProductGroup.UpdateStatus updateStatus = updateProductGroup.getUpdateStatus();

        if(updateStatus.isDescriptionStatus()) productImageUpdateService.updateProductDetailDescription(productGroup, updateProductGroup.getDetailDescription());
        if(updateStatus.isImageStatus()) productImageUpdateService.updateProductImages(productGroup, updateProductGroup.getProductImageList());
        if(updateStatus.isNoticeStatus()) productNoticeUpdateService.updateProductNotice(productGroup, updateProductGroup.getProductNotice());
        if(updateStatus.isDeliveryStatus()) productDeliveryUpdateService.updateProductDeliveryNotice(productGroup, updateProductGroup.getDeliveryNotice());
        if(updateStatus.isRefundStatus()) productDeliveryUpdateService.updateProductRefundNotice(productGroup, updateProductGroup.getRefundNotice());
        if(updateStatus.isStockOptionStatus()) {
            productUpdateService.updateProductAndStock(productGroupId, updateProductGroup.getProductOptions());

            OptionType optionType;
            List<CreateOption> productOptions = updateProductGroup.getProductOptions();
            CreateOption first = productOptions.getFirst();
            List<CreateOptionDetail> options = first.getOptions();

            if(options.isEmpty()) optionType = OptionType.SINGLE;
            else if(options.size() == 1) optionType = OptionType.OPTION_ONE;
            else optionType = OptionType.OPTION_TWO;

            productGroup.setOptionType(optionType);
        }

        if(updateStatus.isProductStatus()) updateProductGroupDetails(productGroup, updateProductGroup.getProductGroupDetails());

        // 트랜잭션 커밋 후 외부몰 동기화 (비동기)
        registerAfterCommitSync(productGroup.getId());

        return productGroup.getId();
    }


    private ProductGroup fetchProductGroupEntity(long productGroupId) {
        return productGroupFetchService.fetchProductGroupEntity(productGroupId);
    }

    private void updateProductGroupDetails(ProductGroup productGroup, ProductGroupDetails productGroupDetails){
        productGroup.updateProductGroupDetails(productGroupDetails);
    }

    private void registerAfterCommitSync(Long productGroupId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                externalMallSyncService.syncProductUpdated(productGroupId);
            }
        });
    }

}

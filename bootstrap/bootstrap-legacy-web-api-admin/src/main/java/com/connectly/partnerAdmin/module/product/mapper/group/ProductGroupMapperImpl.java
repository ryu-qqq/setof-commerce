package com.connectly.partnerAdmin.module.product.mapper.group;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.product.dto.query.CreateClothesDetail;
import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOptionDetail;
import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductStatus;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductDescription;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ClothesDetail;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductGroupDetails;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;
import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;
import com.connectly.partnerAdmin.module.product.entity.score.ProductRatingStats;
import com.connectly.partnerAdmin.module.product.entity.score.ProductScore;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;
import com.connectly.partnerAdmin.module.product.mapper.delivery.ProductDeliveryMapper;
import com.connectly.partnerAdmin.module.product.mapper.image.ProductGroupDetailDescriptionMapper;
import com.connectly.partnerAdmin.module.product.mapper.image.ProductGroupImageMapper;
import com.connectly.partnerAdmin.module.product.mapper.notice.ProductNoticeMapper;
import com.connectly.partnerAdmin.module.product.mapper.stock.ProductMapper;

@Component
@RequiredArgsConstructor
public class ProductGroupMapperImpl implements ProductGroupMapper{

    private final static double DEFAULT_SCORE = 0.0;
    private final static long DEFAULT_COUNT = 0L;
    private static final String  PRODUCT_ID_HEADER= "X-Product-Id";

    private final ProductNoticeMapper productNoticeMapper;
    private final ProductDeliveryMapper productDeliveryMapper;
    private final ProductGroupImageMapper productGroupImageMapper;
    private final ProductGroupDetailDescriptionMapper productGroupDetailDescriptionMapper;
    private final ProductMapper productMapper;


    @Override
    public ProductGroup toProductGroup(String externalProductId, CreateProductGroup createProductGroup) {
        ProductGroup.ProductGroupBuilder productGroupBuilder = ProductGroup.builder()
            .productGroupDetails(toProductGroupDetails(externalProductId, createProductGroup));

        if(createProductGroup.getProductGroupId() != null){
            productGroupBuilder.id(createProductGroup.getProductGroupId());
        }

        ProductGroup productGroup = productGroupBuilder.build();

        ProductDelivery productDelivery = toProductDelivery(createProductGroup.getDeliveryNotice(), createProductGroup.getRefundNotice());
        ProductNotice productNotice = toProductNotice(createProductGroup.getProductNotice());
        List<ProductGroupImage> productGroupImages = toProductGroupImage(createProductGroup.getProductImageList());
        ProductGroupDetailDescription productDetailDescription = toProductDetailDescription(createProductGroup.getDetailDescription());
        ProductRatingStats productRatingStats = new ProductRatingStats(DEFAULT_SCORE, DEFAULT_COUNT);
        Set<Product> products = toProducts(createProductGroup.getProductOptions());

        productGroup.setProductDelivery(productDelivery);
        productGroup.setProductNotice(productNotice);
        productGroup.setDetailDescription(productDetailDescription);
        productGroup.setProductScore(new ProductScore(DEFAULT_SCORE));
        productGroup.setProductRatingStats(productRatingStats);
        productGroupImages.forEach(productGroup::addImage);
        products.forEach(productGroup::addProduct);

        return productGroup;
    }

    private ProductGroupDetails toProductGroupDetails(String externalProductId, CreateProductGroup createProductGroup){

        OptionType optionType;
        List<CreateOption> productOptions = createProductGroup.getProductOptions();
        CreateOption first = productOptions.getFirst();
        List<CreateOptionDetail> options = first.getOptions();

        if(options.isEmpty()) optionType = OptionType.SINGLE;
        else if(options.size() == 1) optionType = OptionType.OPTION_ONE;
        else optionType = OptionType.OPTION_TWO;

        return ProductGroupDetails.builder()
                .productGroupName(createProductGroup.getProductGroupName())
                .sellerId(createProductGroup.getSellerId())
                .brandId(createProductGroup.getBrandId())
                .categoryId(createProductGroup.getCategoryId())
                .optionType(optionType)
                .managementType(createProductGroup.getManagementType())
                .price(getPrice(createProductGroup.getPrice()))
                .clothesDetailInfo(getClothesDetail(createProductGroup.getClothesDetailInfo()))
                .productStatus(getProductStatus(createProductGroup.getProductStatus()))
                .externalProductUuId(externalProductId)
                .build();
    }

    private Price getPrice(CreatePrice createPrice){
        Money regularPrice = createPrice.getRegularPrice();
        Money currentPrice = createPrice.getCurrentPrice();
        return new Price(regularPrice, currentPrice);
    }

    private ClothesDetail getClothesDetail(CreateClothesDetail createClothesDetail){
       return ClothesDetail.builder()
                .productCondition(createClothesDetail.getProductCondition())
                .origin(createClothesDetail.getOrigin())
                .styleCode(createClothesDetail.getStyleCode())
                .build();

    }

    private ProductStatus getProductStatus(CreateProductStatus createProductStatus){
        return new ProductStatus(createProductStatus.getSoldOutYn(), createProductStatus.getDisplayYn());
    }

    private ProductNotice toProductNotice(CreateProductNotice createProductNotice){
        return productNoticeMapper.toProductNotice(createProductNotice);
    }

    private ProductDelivery toProductDelivery(CreateDeliveryNotice createDeliveryNotice, CreateRefundNotice createRefundNotice){
        return productDeliveryMapper.toProductDelivery(createDeliveryNotice, createRefundNotice);
    }

    private List<ProductGroupImage> toProductGroupImage(List<CreateProductImage> productImageList){
        return productGroupImageMapper.toProductGroupImage(productImageList);
    }

    private ProductGroupDetailDescription toProductDetailDescription(String description){
        return productGroupDetailDescriptionMapper.toProductDetailDescription(description);
    }


    private Set<Product> toProducts(List<CreateOption> options){
        return productMapper.toProducts(options);
    }

    @Override
    public UpdateProductGroup toUpdateProductGroup(CreateProductGroup createProductGroup) {

        return UpdateProductGroup.builder()
                .productGroupDetails(toProductGroupDetails(null, createProductGroup))
                .deliveryNotice(createProductGroup.getDeliveryNotice())
                .refundNotice(createProductGroup.getRefundNotice())
                .productNotice(createProductGroup.getProductNotice())
                .productImageList(createProductGroup.getProductImageList())
                .detailDescription(new UpdateProductDescription(createProductGroup.getDetailDescription()))
                .productOptions(createProductGroup.getProductOptions())
                .updateStatus(toCrawlUpdateStatus())
                .build();
    }


    private UpdateProductGroup.UpdateStatus toCrawlUpdateStatus(){
        return UpdateProductGroup.UpdateStatus.builder()
                .productStatus(true)
                .noticeStatus(true)
                .imageStatus(true)
                .descriptionStatus(false)
                .stockOptionStatus(false)
                .deliveryStatus(true)
                .refundStatus(true)
                .build();
    }

}

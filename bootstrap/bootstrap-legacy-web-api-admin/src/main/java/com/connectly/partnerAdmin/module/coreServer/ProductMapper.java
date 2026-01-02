package com.connectly.partnerAdmin.module.coreServer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.product.core.ProductGroupInfo;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.image.ProductImageDto;
import com.connectly.partnerAdmin.module.product.dto.notice.ProductNoticeDto;
import com.connectly.partnerAdmin.module.product.dto.option.OptionDto;
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
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ClothesDetail;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

@Component
public class ProductMapper {

    public CreateProductGroup toProductGroupContextCommandRequestDto(
        ProductGroupFetchResponse productGroupDetailResponse){
        ProductGroupInfo productGroup = productGroupDetailResponse.getProductGroup();
        ProductNoticeDto productNotices = productGroupDetailResponse.getProductNotices();
        DeliveryNotice deliveryNotice = productGroup.getDeliveryNotice();
        ClothesDetail clothesDetailInfo = productGroup.getClothesDetailInfo();
        RefundNotice refundNotice = productGroup.getRefundNotice();
        List<ProductImageDto> productGroupImages = productGroupDetailResponse.getProductGroupImages();
        Set<ProductFetchResponse> products = productGroupDetailResponse.getProducts();

        return new CreateProductGroup(
            productGroup.getProductGroupId(),
            productGroup.getProductGroupName(),
            productGroup.getSellerId(),
            productGroup.getOptionType(),
            productGroup.getManagementType(),
            productGroup.getCategoryId(),
            productGroup.getBrand().getBrandId(),
            new CreateProductStatus(productGroup.getProductStatus().getSoldOutYn(), productGroup.getProductStatus().getDisplayYn()),
            new CreatePrice(Money.wons(productGroup.getPrice().getRegularPrice()), Money.wons(productGroup.getPrice().getCurrentPrice())),
            toCreateProductNotice(productNotices),
            toCreateClothesDetail(clothesDetailInfo),
            toCreateDeliveryNotice(deliveryNotice),
            toCreateRefundNotice(refundNotice),
            toCreateProductImages(productGroupImages),
            productGroupDetailResponse.getDetailDescription(),
            toCreateOptions(products)
        );
    }

    private CreateProductNotice toCreateProductNotice(ProductNoticeDto productNotices){
        return new CreateProductNotice(
            productNotices.getMaterial(),
            productNotices.getColor(),
            productNotices.getSize(),
            productNotices.getMaker(),
            productNotices.getOrigin().getName(),
            productNotices.getWashingMethod(),
            productNotices.getYearMonth(),
            productNotices.getAssuranceStandard(),
            productNotices.getAsPhone()
        );
    }

    private CreateClothesDetail toCreateClothesDetail(ClothesDetail clothesDetailInfo){
        return new CreateClothesDetail(
            clothesDetailInfo.getProductCondition(),
            clothesDetailInfo.getOrigin(),
            clothesDetailInfo.getStyleCode()
        );
    }

    //
    //


    private CreateDeliveryNotice toCreateDeliveryNotice(DeliveryNotice deliveryNotice){
        return new CreateDeliveryNotice(
            deliveryNotice.getDeliveryArea(),
            deliveryNotice.getDeliveryFee(),
            deliveryNotice.getDeliveryPeriodAverage()
        );
    }

    private CreateRefundNotice toCreateRefundNotice(RefundNotice refundNotice){
        return new CreateRefundNotice(
            refundNotice.getReturnMethodDomestic(),
            refundNotice.getReturnCourierDomestic().getCode(),
            refundNotice.getReturnChargeDomestic(),
            refundNotice.getReturnExchangeAreaDomestic()
        );
    }



    private List<CreateProductImage> toCreateProductImages(List<ProductImageDto> productGroupImages) {
        // Main 타입이 이미 처리되었는지 여부를 추적
        boolean mainProcessed = false;

        if(productGroupImages == null || productGroupImages.isEmpty()){
            return List.of(new CreateProductImage(
                ProductGroupImageType.MAIN,
                "image.jpg"
            ));
        }

        // 필터링된 결과를 담을 리스트
        List<CreateProductImage> result = new ArrayList<>();

        for (ProductImageDto productImageDto : productGroupImages) {
            if (productImageDto.getType() == ProductGroupImageType.MAIN) {
                // Main 타입은 하나만 추가
                if (!mainProcessed) {
                    result.add(new CreateProductImage(
                        productImageDto.getType(),
                        productImageDto.getProductImageUrl()
                    ));
                    mainProcessed = true;
                }
            } else {
                // Main 타입 이후에는 Detail 타입을 추가
                result.add(new CreateProductImage(
                    productImageDto.getType(),
                    productImageDto.getProductImageUrl()
                ));
            }
        }

        if (result.size() > 10) {
            // 메인 이미지 추출
            List<CreateProductImage> mainImages = result.stream()
                .filter(i -> i.getProductImageType().isMain())
                .distinct() // 중복 제거
                .toList();

            // 메인 이미지를 제외한 나머지 이미지 추출
            List<CreateProductImage> nonMainImages = result.stream()
                .filter(i -> !i.getProductImageType().isMain())
                .toList();

            // 메인 이미지를 우선적으로 포함하고, 나머지 이미지를 추가하여 최대 10개로 제한
            result = Stream.concat(mainImages.stream(), nonMainImages.stream())
                .distinct() // 중복 제거
                .limit(10) // 최대 10개로 제한
                .toList();
        }


        return result;
    }


    private List<CreateOption> toCreateOptions(Set<ProductFetchResponse> products){

        OptionType optionType = OptionType.SINGLE;
        Optional<ProductFetchResponse> first = products.stream().findFirst();
        if(first.isPresent()){
            ProductFetchResponse productFetchResponse = first.get();
            Set<OptionDto> options = productFetchResponse.getOptions();

            if(options.size() == 2){
                optionType = OptionType.OPTION_TWO;
            }else if (options.size() == 1){
                optionType = OptionType.OPTION_ONE;
            }
        }


        if(products.isEmpty()){
            List<CreateOption> free;
            if(optionType.isOneDepth()){
                free = List.of(new CreateOption(null, 1,  BigDecimal.ZERO, List.of(new CreateOptionDetail(null, null, OptionName.SIZE, "FREE"))));
            }else if(optionType.isTwoDepth()){
                free = List.of(new CreateOption(null, 1,  BigDecimal.ZERO, List.of(new CreateOptionDetail(null, null, OptionName.SIZE, "FREE"), new CreateOptionDetail(null, null, OptionName.COLOR, "FREE"))));
            }else{
                free = List.of();
            }
            return List.of(new CreateOption(null, 1,  BigDecimal.ZERO, List.of()));
        }


        if(optionType.isOneDepth()){
            return products.stream()
                .filter(p -> p.getOptions().size() == 1)
                .map(p -> new CreateOption(
                    null,
                    Math.min(p.getStockQuantity(), 999),
                    p.getAdditionalPrice(),
                    toProductOptionInsertRequestDtos(p.getOptions())
                )).toList();
        }

        if(optionType.isTwoDepth()){
            return products.stream()
                .filter(p -> p.getOptions().size() == 2)
                .map(p -> new CreateOption(
                    null,
                    Math.min(p.getStockQuantity(), 999),
                    p.getAdditionalPrice(),
                    toProductOptionInsertRequestDtos(p.getOptions())
                )).toList();
        }

        return products.stream()
            .map(p -> new CreateOption(
                null, Math.min(p.getStockQuantity(), 999), p.getAdditionalPrice(), toProductOptionInsertRequestDtos(p.getOptions())
            )).toList();

    }

    private List<CreateOptionDetail> toProductOptionInsertRequestDtos(Set<OptionDto> options){
        return options.stream()
            .map(o -> new CreateOptionDetail(
                null,
                null,
                o.getOptionName(),
                o.getOptionValue()
            ))
            .toList();
    }
}

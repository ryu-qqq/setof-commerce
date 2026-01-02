package com.connectly.partnerAdmin.module.coreServer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.coreServer.request.CoreCreateProductGroup;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductDeliveryRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductDetailDescriptionRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductGroupContextCommandRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductGroupImageRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductGroupInsertRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductInsertRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductNoticeInsertRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductOptionInsertRequestDto;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackErrorIssueService;
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
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductStatus;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ClothesDetail;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

@Component
public class CoreServerProductMapper {

    private final SlackErrorIssueService errorIssueService;

    public CoreServerProductMapper(SlackErrorIssueService errorIssueService) {
        this.errorIssueService = errorIssueService;
    }

    public CoreCreateProductGroup toProductGroupContextCommandRequestDto(ProductGroupFetchResponse productGroupDetailResponse){
        ProductGroupInfo productGroup = productGroupDetailResponse.getProductGroup();
        ProductNoticeDto productNotices = productGroupDetailResponse.getProductNotices();
        DeliveryNotice deliveryNotice = productGroup.getDeliveryNotice();
        ClothesDetail clothesDetailInfo = productGroup.getClothesDetailInfo();
        RefundNotice refundNotice = productGroup.getRefundNotice();
        List<ProductImageDto> productGroupImages = productGroupDetailResponse.getProductGroupImages();
        Set<ProductFetchResponse> products = productGroupDetailResponse.getProducts();

        return new CoreCreateProductGroup(
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
            new ProductDetailDescriptionRequestDto(productGroupDetailResponse.getDetailDescription()),
            toCreateOptions(products)
        );
    }

    public ProductGroupContextCommandRequestDto toCommand(ProductGroupFetchResponse productGroupFetchResponse){
        ProductGroupInfo productGroup = productGroupFetchResponse.getProductGroup();

        ProductGroupInsertRequestDto productGroupInsertRequestDto = new ProductGroupInsertRequestDto(
            productGroupFetchResponse.getProductGroupId(),
            productGroup.getBrand().getBrandId(),
            productGroup.getCategoryId(),
            productGroup.getSellerId(),
            productGroup.getProductGroupName(),
            productGroup.getClothesDetailInfo().getStyleCode(),
            ProductCondition.NEW,
            productGroup.getManagementType(),
            productGroup.getOptionType(),
            productGroup.getPrice().getRegularPrice(),
            productGroup.getPrice().getCurrentPrice(),
            productGroup.getProductStatus().isSoldOut(),
            true,
            ""
        );

        ProductNoticeInsertRequestDto productNoticeInsertRequestDto = new ProductNoticeInsertRequestDto(
            productGroupFetchResponse.getProductNotices().getMaterial(),
            productGroupFetchResponse.getProductNotices().getColor(),
            productGroupFetchResponse.getProductNotices().getSize(),
            productGroupFetchResponse.getProductNotices().getMaker(),
            productGroupFetchResponse.getProductNotices().getOrigin(),
            productGroupFetchResponse.getProductNotices().getWashingMethod(),
            productGroupFetchResponse.getProductNotices().getYearMonth(),
            productGroupFetchResponse.getProductNotices().getAssuranceStandard(),
            productGroupFetchResponse.getProductNotices().getAsPhone()
        );

        ProductDeliveryRequestDto productDeliveryRequestDto = new ProductDeliveryRequestDto(
            productGroup.getDeliveryNotice().getDeliveryArea(),
            BigDecimal.valueOf(productGroup.getDeliveryNotice().getDeliveryFee()),
            productGroup.getDeliveryNotice().getDeliveryPeriodAverage(),
            productGroup.getRefundNotice().getReturnMethodDomestic(),
            productGroup.getRefundNotice().getReturnCourierDomestic(),
            BigDecimal.valueOf(productGroup.getRefundNotice().getReturnChargeDomestic()),
            productGroup.getRefundNotice().getReturnExchangeAreaDomestic()
        );

        ProductDetailDescriptionRequestDto productDetailDescriptionRequestDto = new ProductDetailDescriptionRequestDto(
            productGroupFetchResponse.getDetailDescription()
        );

        return new ProductGroupContextCommandRequestDto(
            productGroupInsertRequestDto,
            productNoticeInsertRequestDto,
            productDeliveryRequestDto,
            toImageCommand(productGroupFetchResponse.getProductGroupImages()),
            productDetailDescriptionRequestDto,
            toOptionCommand(productGroup.getOptionType(), productGroupFetchResponse.getProducts())
        );
    }

    public List<ProductInsertRequestDto> toOptionCommand(OptionType optionType, Set<ProductFetchResponse> requestDto) {

        return requestDto.stream()
            .map(p -> new ProductInsertRequestDto(
                p.getProductId(),
                p.getProductStatus().isSoldOut(),
                true,
                p.getStockQuantity(),
                p.getAdditionalPrice(),
                toOptionContexts(p.getOptions())
            )
            )
            .toList();
    }

    private static List<ProductOptionInsertRequestDto> toOptionContexts(Set<OptionDto> options) {
        return options.stream()
            .map(o -> new ProductOptionInsertRequestDto(o.getOptionName(), o.getOptionValue()))
            .distinct()
            .collect(Collectors.toList());
    }

    public List<ProductGroupImageRequestDto> toImageCommand(List<ProductImageDto> productGroupImages) {
        List<ProductGroupImageRequestDto> productGroupImageCommands = new ArrayList<>();

        for (ProductImageDto img : productGroupImages) {
            if (img.getType().isMain()) {
                productGroupImageCommands.add(
                    new ProductGroupImageRequestDto(
                        img.getType(),
                        img.getProductImageUrl()
                    ));
            } else {
                productGroupImageCommands.add(
                    new ProductGroupImageRequestDto(
                        img.getType(),
                        img.getProductImageUrl()
                    ));
            }
        }

        return productGroupImageCommands;
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
        if(options.size() == 1){
            Optional<OptionDto> first = options.stream().findFirst();
            OptionDto optionDto = first.get();
            if(optionDto.getOptionName().equals(OptionName.DEFAULT_TWO)){
                return options.stream()
                    .map(o -> new CreateOptionDetail(
                        null,
                        null,
                        OptionName.DEFAULT_ONE,
                        o.getOptionValue()
                    ))
                    .toList();
            }
        }

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

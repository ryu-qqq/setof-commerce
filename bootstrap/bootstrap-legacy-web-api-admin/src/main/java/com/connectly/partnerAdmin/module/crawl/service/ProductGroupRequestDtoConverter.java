package com.connectly.partnerAdmin.module.crawl.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.crawl.dto.request.CrawlProductGroupInsertRequestDto;
import com.connectly.partnerAdmin.module.crawl.dto.request.Option;
import com.connectly.partnerAdmin.module.crawl.enums.MustItSellerId;
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
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

public class ProductGroupRequestDtoConverter {


    public static CreateProductGroup toRequestDto(CrawlProductGroupInsertRequestDto requestDto, long brandId, long categoryId) {
        MustItSellerId mustItSellerId = MustItSellerId.fromOriginalId(requestDto.getSellerId());

        CreateRefundNotice refundNotice = SellerReturnInfo.getRefundNotice(mustItSellerId.getMappedId());

        return new CreateProductGroup(
            null,
            requestDto.getItemName(),
            mustItSellerId.getMappedId(),
            getOptionType(requestDto.getOptions()),
            ManagementType.AUTO,
            categoryId,
            brandId,
            toCreateProductStatus(requestDto),
            toCreatePrice(requestDto),
            toCreateProductNotice(),
            new CreateClothesDetail(ProductCondition.NEW, Origin.OTHER, ""),
            new CreateDeliveryNotice(refundNotice.getReturnExchangeAreaDomestic(), 0, 2),
            refundNotice,
            toProductImageList(requestDto),
            requestDto.getDescriptionMarkUp(),
            getProductOptions(requestDto.getOptions())
        );

    }




    public static CreatePrice toCreatePrice(CrawlProductGroupInsertRequestDto dto) {

        double discountPrice = Optional.ofNullable(dto.getPrice()).orElse(0);
        double sellingPrice = Optional.ofNullable(dto.getOriginalPrice()).orElse(0);
        double normalPrice = Optional.ofNullable(dto.getNormalPrice()).orElse(0);

        int regularPrice = 0;
        int currentPrice = 0;

        if (discountPrice > 0 && normalPrice == 0 && sellingPrice == 0) {
            regularPrice = (int) discountPrice;
            currentPrice = (int) discountPrice;

        } else if (discountPrice > 0 && normalPrice > 0 && sellingPrice == 0) {
            regularPrice = (int) normalPrice;
            currentPrice = (int) discountPrice;

        } else if (discountPrice > 0 && normalPrice == 0 && sellingPrice > 0) {
            regularPrice = (int) sellingPrice;
            currentPrice = (int) sellingPrice;

        } else if (normalPrice > 0 && sellingPrice > 0 && discountPrice > 0) {
            regularPrice = (int) normalPrice;
            currentPrice = (int) sellingPrice;
        }

        return new CreatePrice(Money.wons(regularPrice), Money.wons(currentPrice));
    }



    public static List<CreateProductImage> toProductImageList(CrawlProductGroupInsertRequestDto dto) {
        List<CreateProductImage> images = new ArrayList<>();
        boolean isMain = true;
        for(String image : dto.getImages()) {
            images.add(
                new CreateProductImage(
                    isMain ? ProductGroupImageType.MAIN : ProductGroupImageType.DETAIL,
                    image
                )
            );
            isMain = false;
        }
        return images;
    }


    public static CreateProductStatus toCreateProductStatus(CrawlProductGroupInsertRequestDto dto){
        Boolean soldOut = dto.getIsSoldOut();

        if(soldOut){
            return new CreateProductStatus(Yn.Y, Yn.Y);
        }else{
            return new CreateProductStatus(Yn.N, Yn.Y);
        }
    }

    public static CreateProductNotice toCreateProductNotice(){
        return new CreateProductNotice(
            "상세 설명 참조",
            "상세 설명 참조",
            "상세 설명 참조",
            "상세 설명 참조",
            "상세 설명 참조",
            "상세 설명 참조",
            "상세 설명 참조",
            "관련법 및 소비자 분쟁해결 기준 따름",
            "상세 설명 참조"

        );
    }


    public static List<CreateOption> getProductOptions(List<Option> mustItOptions) {
        List<CreateOption> productOptions = new ArrayList<>();

        for (Option option : mustItOptions) {
            String size = Optional.ofNullable(option.size()).orElse("");
            String color = Optional.ofNullable(option.color()).orElse("");
            int stock = option.stock();

            // 옵션이 아예 없을 경우
            if (size.isEmpty() && color.isEmpty()) {
                return List.of(new CreateOption(null, stock, BigDecimal.ZERO, List.of()));
            }

            List<CreateOptionDetail> optionDetails = new ArrayList<>();

            if (!color.isEmpty() && size.isEmpty()) {
                optionDetails.add(new CreateOptionDetail(null, null, OptionName.COLOR, color));
            }

            if (!size.isEmpty() && color.isEmpty()) {
                optionDetails.add(new CreateOptionDetail(null, null,OptionName.SIZE, size));
            }

            if (!color.isEmpty() && !size.isEmpty()) {
                optionDetails.add(new CreateOptionDetail(null, null,OptionName.COLOR, color));
                optionDetails.add(new CreateOptionDetail(null, null,OptionName.SIZE, size));
            }

            productOptions.add(new CreateOption(null, stock, BigDecimal.ZERO, optionDetails));
        }

        return productOptions;
    }

    public static OptionType getOptionType(List<Option> mustItOptions) {

        Option first = mustItOptions.getFirst();

        if(first == null) return OptionType.SINGLE;

        if (first.size() == null && first.color() == null) {
            return OptionType.SINGLE;
        }

        if (first.size() != null && first.color() != null) {
            return OptionType.OPTION_TWO;
        }

        return OptionType.OPTION_ONE;
    }



}

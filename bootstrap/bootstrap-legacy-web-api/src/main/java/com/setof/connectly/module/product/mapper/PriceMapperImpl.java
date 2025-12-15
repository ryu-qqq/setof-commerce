package com.setof.connectly.module.product.mapper;

import com.setof.connectly.module.discount.dto.DiscountCalculateDto;
import com.setof.connectly.module.product.dto.price.ProductGroupPriceDto;
import org.springframework.stereotype.Component;

@Component
public class PriceMapperImpl implements PriceMapper {

    public DiscountCalculateDto toDiscountRedisFetchDto(ProductGroupPriceDto productGroupPrice) {
        return DiscountCalculateDto.builder()
                .productGroupId(productGroupPrice.getProductGroupId())
                .sellerId(productGroupPrice.getSellerId())
                .price(productGroupPrice.getPrice())
                .build();
    }
}

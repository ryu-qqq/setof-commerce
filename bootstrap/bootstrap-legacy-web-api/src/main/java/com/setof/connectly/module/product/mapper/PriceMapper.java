package com.setof.connectly.module.product.mapper;

import com.setof.connectly.module.discount.dto.DiscountCalculateDto;
import com.setof.connectly.module.product.dto.price.ProductGroupPriceDto;

public interface PriceMapper {
    DiscountCalculateDto toDiscountRedisFetchDto(ProductGroupPriceDto productGroupPrice);
}

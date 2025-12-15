package com.setof.connectly.module.product.service.price;

import com.setof.connectly.module.product.dto.price.ProductGroupPriceDto;
import java.util.List;

public interface ProductPriceFindService {

    ProductGroupPriceDto fetchProductGroupPrice(long productIds);

    List<ProductGroupPriceDto> fetchProductGroupPrices(List<Long> productIds);
}

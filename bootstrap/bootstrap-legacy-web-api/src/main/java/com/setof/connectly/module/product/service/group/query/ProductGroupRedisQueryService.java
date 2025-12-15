package com.setof.connectly.module.product.service.group.query;

import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;

public interface ProductGroupRedisQueryService {

    void saveSellerProductGroupThumbnail(
            long sellerId, List<ProductGroupThumbnail> productGroupThumbnails);

    void saveBrandProductGroupThumbnail(
            long brandId, List<ProductGroupThumbnail> productGroupThumbnails);
}

package com.setof.connectly.module.product.service.group.fetch;

import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;

public interface SellerProductRedisFindService {

    List<ProductGroupThumbnail> fetchProductGroupWithSeller(long sellerId);
}

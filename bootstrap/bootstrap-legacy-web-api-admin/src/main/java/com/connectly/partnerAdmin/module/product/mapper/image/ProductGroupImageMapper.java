package com.connectly.partnerAdmin.module.product.mapper.image;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;

import java.util.List;

public interface ProductGroupImageMapper {

    List<ProductGroupImage> toProductGroupImage(List<CreateProductImage> productImageList);

}

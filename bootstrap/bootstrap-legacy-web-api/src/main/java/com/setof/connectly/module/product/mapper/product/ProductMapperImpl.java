package com.setof.connectly.module.product.mapper.product;

import com.setof.connectly.module.product.dto.group.ProductDto;
import com.setof.connectly.module.product.dto.group.ProductGroupPatchResponse;
import com.setof.connectly.module.product.entity.group.Product;
import com.setof.connectly.module.product.entity.group.ProductGroup;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductGroupPatchResponse toProductPatchResponse(Product productEntity) {
        ProductGroup productGroup = productEntity.getProductGroup();

        ProductDto product =
                ProductDto.builder()
                        .productId(productEntity.getId())
                        .stockQuantity(productEntity.getStockQuantity())
                        .productStatus(productEntity.getProductStatus())
                        .build();

        return ProductGroupPatchResponse.builder()
                .productGroupId(productGroup.getId())
                .productGroupDetails(productGroup.getProductGroupDetails())
                .products(Collections.singleton(product))
                .build();
    }
}

package com.connectly.partnerAdmin.module.product.mapper.image;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;
import com.connectly.partnerAdmin.module.product.entity.image.embedded.ImageDetail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductGroupImageMapperImpl implements ProductGroupImageMapper {

    @Override
    public List<ProductGroupImage> toProductGroupImage(List<CreateProductImage> productImageList) {
        List<ProductGroupImage> result = new ArrayList<>();
        for (int i = 0; i < productImageList.size(); i++) {
            CreateProductImage image = productImageList.get(i);
            String originUrl = image.getOriginUrl() != null ? image.getOriginUrl() : image.getImageUrl();
            ProductGroupImage productGroupImage = ProductGroupImage.builder()
                    .imageDetail(new ImageDetail(
                            image.getProductImageType(),
                            image.getImageUrl(),
                            originUrl,
                            i
                    ))
                    .build();
            result.add(productGroupImage);
        }
        return result;
    }

}

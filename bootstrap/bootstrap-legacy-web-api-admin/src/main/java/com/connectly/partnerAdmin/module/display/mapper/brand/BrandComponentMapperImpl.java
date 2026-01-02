package com.connectly.partnerAdmin.module.display.mapper.brand;

import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.item.BrandComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.BrandComponent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandComponentMapperImpl implements BrandComponentMapper{


    @Override
    public BrandComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component) {
        return BrandComponent.builder()
                .component(component)
                .build();

    }

    @Override
    public List<BrandComponentItem> toBrandComponentItems(long brandComponentId, BrandComponentDetail brandComponentDetail) {
        return brandComponentDetail.getBrandIdList()
                .stream()
                .map(brandId ->{
                    return BrandComponentItem.builder()
                            .brandComponentId(brandComponentId)
                            .brandId(brandId)
                            .categoryId(brandComponentDetail.getCategoryId())
                            .build();

                }).collect(Collectors.toList());

    }
}

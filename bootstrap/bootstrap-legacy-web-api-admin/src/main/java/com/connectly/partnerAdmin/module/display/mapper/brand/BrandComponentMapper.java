package com.connectly.partnerAdmin.module.display.mapper.brand;

import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.item.BrandComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.BrandComponent;

import java.util.List;

public interface BrandComponentMapper {
    BrandComponent toEntity(Component component);
    List<BrandComponentItem> toBrandComponentItems(long brandComponentId, BrandComponentDetail brandComponentDetail);
}

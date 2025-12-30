package com.connectly.partnerAdmin.module.display.repository.component.brand;

import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.item.BrandComponentItem;

import java.util.List;

public interface BrandComponentItemJdbcRepository {

    void saveAll(List<BrandComponentItem> brandComponentItems);
    void deleteAll(List<BrandComponentDetail> deleteBrandComponentItems);
    void addAll(List<BrandComponentDetail> toAddBrandComponentItems);

    void updateCategoryIdAll(List<BrandComponentDetail> toUpdateBrandComponentItems);

}

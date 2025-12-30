package com.connectly.partnerAdmin.module.brand.service;

import com.connectly.partnerAdmin.module.brand.core.BrandMappingInfo;

import java.util.List;

public interface BrandMappingManager {

    void convertExternalBrandToInternalBrand(long siteId, List<BrandMappingInfo> brandMappingInfos);

}

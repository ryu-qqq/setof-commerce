package com.connectly.partnerAdmin.module.external.mapper;

import com.connectly.partnerAdmin.module.external.core.InterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.SellerExternalIntegrationDto;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;

import java.util.List;

public interface ExternalProductMapper {

    List<ExternalProduct> toInterLockingProductMappings(List<? extends InterlockingProduct> interLockingProducts, List<SellerExternalIntegrationDto> interLockingSites);

}

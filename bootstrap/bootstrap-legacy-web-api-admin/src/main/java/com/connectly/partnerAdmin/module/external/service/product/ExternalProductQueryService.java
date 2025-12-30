package com.connectly.partnerAdmin.module.external.service.product;

import com.connectly.partnerAdmin.module.external.core.InterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductUpdateDto;

import java.util.List;

public interface ExternalProductQueryService {

    void productSync(long sellerId);

    void registrationSync(List<? extends InterlockingProduct> interLockingProducts);

    long updateMappingStatus(List<ExternalProductUpdateDto> externalProductUpdates, long siteId);

}

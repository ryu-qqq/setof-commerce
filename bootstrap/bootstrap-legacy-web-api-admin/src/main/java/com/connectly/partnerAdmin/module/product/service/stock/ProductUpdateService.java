package com.connectly.partnerAdmin.module.product.service.stock;

import java.util.List;
import java.util.Set;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateDisplayYn;

public interface ProductUpdateService {

    Set<ProductFetchResponse> updateProductAndStock(long productGroupId, List<CreateOption> options);
    long updateIndividualDisplayYn(long productId, UpdateDisplayYn updateDisplayYn);


}

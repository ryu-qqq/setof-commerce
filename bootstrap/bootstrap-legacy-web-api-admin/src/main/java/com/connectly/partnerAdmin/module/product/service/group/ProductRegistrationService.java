package com.connectly.partnerAdmin.module.product.service.group;

import java.util.List;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroupResponse;

public interface ProductRegistrationService {

    CreateProductGroupResponse registerProduct(String externalProductId, CreateProductGroup createProductGroup);

    List<Long> registerProducts(String externalProductId, List<CreateProductGroup> createProductGroup);

}

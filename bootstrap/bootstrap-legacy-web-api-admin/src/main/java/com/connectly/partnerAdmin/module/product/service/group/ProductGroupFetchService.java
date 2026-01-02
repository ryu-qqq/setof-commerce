package com.connectly.partnerAdmin.module.product.service.group;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.product.core.ExternalContext;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupInfoDto;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter;

public interface ProductGroupFetchService {

    ProductGroupFetchResponse fetchProductGroup(long productGroupId);
    List<Long> fetchIds();

    List<ProductGroupInfoDto> fetchProductGroup(String externalProductUuId);
    CustomPageable<ProductGroupDetailResponse> fetchProductGroups(ProductGroupFilter filter, Pageable pageable);

    List<ProductGroupDetailResponse> fetchProductGroups(List<Long> productGroupIds);

    ProductGroup fetchProductGroupEntity(long productGroupId);

    List<ProductGroupDetailResponse> fetchProductGroups(Long offset, int size, boolean desc, List<Long> sellerIds);

    List<ExternalContext> fetchExternalProducts(long siteId);

}

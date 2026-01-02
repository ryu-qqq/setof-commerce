package com.connectly.partnerAdmin.module.external.service.product;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.external.core.BaseInterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductInfoDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductMappingDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductSiteDto;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.filter.ExternalProductFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExternalProductFetchService {

    List<ExternalProduct> fetchExternalProductEntities(long productGroupId);
    List<ExternalProduct> fetchExternalProductEntities(long siteId, List<Long> productGroupIds);


    List<ExternalProductSiteDto> fetchHasSyncExternalProducts(List<Long> productGroupIds, List<Long> siteIds);
    List<ExternalProductMappingDto> fetchHasExistingExternalProducts(List<String> externalIds, long siteId);
    List<BaseInterlockingProduct> fetchProductMapping(long sellerId);
    Optional<ExternalProduct> fetchHasExternalProductEntity(long productGroupId, long siteId);

    CustomPageable<ExternalProductInfoDto> fetchExternalProducts(ExternalProductFilter filter, Pageable pageable);


}

package com.connectly.partnerAdmin.module.external.repository.product;

import com.connectly.partnerAdmin.module.external.core.BaseInterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductInfoDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductMappingDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductSiteDto;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.filter.ExternalProductFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExternalProductFetchRepository {
    List<ExternalProduct> fetchExternalProductEntities(long productGroupId);
    List<ExternalProduct> fetchExternalProductEntities(long siteId, List<Long> productGroupIds);


    List<ExternalProductSiteDto> fetchHasSyncExternalProducts(List<Long> productGroupIds, List<Long> siteIds);
    List<ExternalProductMappingDto> fetchHasExistingExternalProducts(List<String> externalIds, long siteId);

    List<BaseInterlockingProduct> fetchProductMappings(long sellerId);

    Optional<ExternalProduct> fetchHasExternalProductEntity(long productGroupId, long siteId);

    List<ExternalProductInfoDto> fetchExternalProductInfos(ExternalProductFilter filter, Pageable pageable);

}


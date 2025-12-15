package com.setof.connectly.module.product.service.group.fetch;

import com.setof.connectly.module.common.dto.CursorDto;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.product.dto.ProductGroupResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.filter.ProductFilter;
import com.setof.connectly.module.product.dto.group.fetch.ProductGroupFetchDto;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ProductGroupFindService {

    ProductGroupFetchDto fetchProductGroupDto(long productGroupId);

    ProductGroupResponse fetchProductGroup(long productGroupId);

    List<ProductGroupThumbnail> fetchProductGroupRecent(List<Long> productGroupIds);

    CustomSlice<ProductGroupThumbnail> fetchProductGroups(ProductFilter filter, Pageable pageable);

    List<ProductGroupThumbnail> fetchProductGroupWithBrand(long brandId, Pageable pageable);

    List<ProductGroupThumbnail> fetchProductGroupWithSeller(long sellerId, Pageable pageable);

    Optional<CursorDto> fetchLastProductGroupId(
            ComponentFilter filter, Set<Long> exclusiveProductIds);

    long fetchProductCountQuery(ComponentFilter filter);

    List<Long> fetchProductGroupIds(List<Long> productIds);
}

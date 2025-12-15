package com.setof.connectly.module.product.repository.group;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.common.dto.CursorDto;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.filter.ProductFilter;
import com.setof.connectly.module.product.dto.group.fetch.ProductGroupFetchDto;
import com.setof.connectly.module.product.dto.price.ProductGroupPriceDto;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ProductGroupFindRepository {
    Optional<ProductGroupFetchDto> fetchProductGroupDto(long productGroupId);

    Optional<ProductGroupPriceDto> fetchProductGroupPrice(long productId);

    List<ProductGroupPriceDto> fetchProductGroupPrices(List<Long> productIds);

    Optional<CursorDto> fetchLastProductGroupId(
            ComponentFilter filter, Set<Long> exclusiveProductIds);

    JPAQuery<Long> fetchProductCountQuery(ComponentFilter filter);

    List<ProductGroupThumbnail> fetchProductsWithSeller(long sellerId, Pageable pageable);

    List<ProductGroupThumbnail> fetchProductsWithBrand(long brandId, Pageable pageable);

    List<ProductGroupThumbnail> fetchProductGroups(ProductFilter filter, int pageSize);

    List<ProductGroupThumbnail> fetchProductGroupsRecent(List<Long> productGroupIds);

    JPAQuery<Long> fetchProductCountQuery(ProductFilter filter);

    List<Long> fetchProductGroupIds(List<Long> productIds);
}

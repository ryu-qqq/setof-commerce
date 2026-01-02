package com.connectly.partnerAdmin.module.product.repository.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.product.core.ExternalContext;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter;
import com.querydsl.jpa.impl.JPAQuery;

public interface ProductGroupFetchRepository {

    Optional<ProductGroupFetchResponse> fetchProductGroup(long productGroupId, Optional<Long> sellerId);
    List<ProductGroupFetchResponse> fetchProductGroup(String externalProductUuId);

    List<Long> fetchProductGroupIds();

    JPAQuery<Long> fetchProductGroupCountQuery(ProductGroupFilter dto);
    List<ProductGroupDetailResponse> fetchProductGroups(ProductGroupFilter filter, Pageable pageable);
    List<ProductGroupDetailResponse> fetchProductsWithNoOffset(ProductGroupFilter filter, Pageable pageable);
    Optional<ProductGroup> fetchProductGroupEntity(long productGroupId);


    List<ProductGroupDetailResponse> fetchProductGroups(List<Long> productGroupIds);
    List<ProductGroupDetailResponse> fetchProductsWithNoOffset(Long offset, int size, List<Long> sellerIds);
    List<ProductGroupDetailResponse> fetchProductsWithNoOffsetAsc(Long offset, int size, List<Long> sellerIds);

    List<ExternalContext> fetchExternalProducts(long siteId);

}

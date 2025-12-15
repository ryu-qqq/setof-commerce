package com.setof.connectly.module.product.service.group.fetch;

import com.setof.connectly.module.common.dto.CursorDto;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.exception.product.ProductGroupNotFoundException;
import com.setof.connectly.module.product.dto.ProductGroupResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.filter.ProductFilter;
import com.setof.connectly.module.product.dto.group.fetch.ProductGroupFetchDto;
import com.setof.connectly.module.product.mapper.ProductGroupMapper;
import com.setof.connectly.module.product.mapper.ProductSliceMapper;
import com.setof.connectly.module.product.repository.group.ProductGroupFindRepository;
import com.setof.connectly.module.product.service.category.ProductCategoryFetchService;
import com.setof.connectly.module.product.service.group.query.ProductGroupRedisQueryService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductGroupFindServiceImpl implements ProductGroupFindService {
    private final ProductGroupFindRepository productGroupFindRepository;
    private final ProductGroupMapper productFetchMapper;
    private final ProductSliceMapper productSliceMapper;
    private final BrandProductRedisFindService brandProductRedisFindService;
    private final SellerProductRedisFindService sellerProductRedisFindService;
    private final ProductGroupRedisQueryService productGroupRedisQueryService;
    private final ProductCategoryFetchService productCategoryFetchService;

    @Override
    public ProductGroupResponse fetchProductGroup(long productGroupId) {
        ProductGroupFetchDto productGroup = fetchProductGroupDto(productGroupId);
        List<ProductCategoryDto> productCategoryList =
                getCategoryListForProductGroup(productGroup.getPath());
        return productFetchMapper.toProductGroupResponse(productGroup, productCategoryList);
    }

    @Override
    public List<ProductGroupThumbnail> fetchProductGroupRecent(List<Long> productGroupIds) {
        List<ProductGroupThumbnail> productGroupThumbnails =
                productGroupFindRepository.fetchProductGroupsRecent(productGroupIds);
        return productFetchMapper.reOrderProductGroupThumbnail(
                productGroupIds, productGroupThumbnails);
    }

    @Override
    public CustomSlice<ProductGroupThumbnail> fetchProductGroups(
            ProductFilter filter, Pageable pageable) {
        List<ProductGroupThumbnail> productGroupThumbnails =
                productGroupFindRepository.fetchProductGroups(filter, pageable.getPageSize());
        long totalElements = fetchProductCountQuery(filter);
        return productSliceMapper.toSlice(productGroupThumbnails, pageable, totalElements, filter);
    }

    @Override
    public List<ProductGroupThumbnail> fetchProductGroupWithBrand(long brandId, Pageable pageable) {
        List<ProductGroupThumbnail> productGroupThumbnails =
                brandProductRedisFindService.fetchProductGroupWithBrand(brandId);
        if (!productGroupThumbnails.isEmpty()) return productGroupThumbnails;
        return fetchProductGroupWithBrandInDb(brandId, pageable);
    }

    @Override
    public List<ProductGroupThumbnail> fetchProductGroupWithSeller(
            long sellerId, Pageable pageable) {
        List<ProductGroupThumbnail> productGroupThumbnails =
                sellerProductRedisFindService.fetchProductGroupWithSeller(sellerId);
        if (!productGroupThumbnails.isEmpty()) return productGroupThumbnails;
        return fetchProductGroupWithSellerInDb(sellerId, pageable);
    }

    private List<ProductGroupThumbnail> fetchProductGroupWithBrandInDb(
            long brandId, Pageable pageable) {
        List<ProductGroupThumbnail> productGroupThumbnails =
                productGroupFindRepository.fetchProductsWithBrand(brandId, pageable);
        productGroupRedisQueryService.saveBrandProductGroupThumbnail(
                brandId, productGroupThumbnails);
        return productGroupThumbnails;
    }

    private List<ProductGroupThumbnail> fetchProductGroupWithSellerInDb(
            long sellerId, Pageable pageable) {
        List<ProductGroupThumbnail> productGroupThumbnails =
                productGroupFindRepository.fetchProductsWithSeller(sellerId, pageable);
        productGroupRedisQueryService.saveSellerProductGroupThumbnail(
                sellerId, productGroupThumbnails);
        return productGroupThumbnails;
    }

    private long fetchProductCountQuery(ProductFilter filter) {
        return productGroupFindRepository.fetchProductCountQuery(filter).fetchCount();
    }

    @Override
    public ProductGroupFetchDto fetchProductGroupDto(long productGroupId) {
        return productGroupFindRepository
                .fetchProductGroupDto(productGroupId)
                .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId));
    }

    @Override
    public Optional<CursorDto> fetchLastProductGroupId(
            ComponentFilter filter, Set<Long> exclusiveProductIds) {
        return productGroupFindRepository.fetchLastProductGroupId(filter, exclusiveProductIds);
    }

    @Override
    public long fetchProductCountQuery(ComponentFilter filter) {
        return productGroupFindRepository.fetchProductCountQuery(filter).fetchCount();
    }

    @Override
    public List<Long> fetchProductGroupIds(List<Long> productIds) {
        return productGroupFindRepository.fetchProductGroupIds(productIds);
    }

    private List<ProductCategoryDto> getCategoryListForProductGroup(String path) {
        return productCategoryFetchService.fetchProductCategories(path);
    }
}

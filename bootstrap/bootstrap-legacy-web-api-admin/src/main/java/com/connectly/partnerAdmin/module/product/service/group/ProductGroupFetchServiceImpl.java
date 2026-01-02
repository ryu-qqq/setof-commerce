package com.connectly.partnerAdmin.module.product.service.group;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.category.service.CategoryFetchService;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.product.core.ExternalContext;
import com.connectly.partnerAdmin.module.product.core.ProductGroupInfo;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupInfoDto;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.exception.ProductNotFoundException;
import com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter;
import com.connectly.partnerAdmin.module.product.mapper.group.ProductGroupCategoryMapper;
import com.connectly.partnerAdmin.module.product.mapper.group.ProductGroupPageableMapper;
import com.connectly.partnerAdmin.module.product.repository.group.ProductGroupFetchRepository;
import com.connectly.partnerAdmin.module.product.service.stock.ProductFetchService;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;

import static com.connectly.partnerAdmin.module.product.exception.ProductErrorConstant.PRODUCT_GROUP_NOT_FOUND_MSG;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductGroupFetchServiceImpl implements ProductGroupFetchService{

    private final ProductGroupFetchRepository productGroupFetchRepository;
    private final CategoryFetchService categoryFetchService;
    private final ProductFetchService productFetchService;
    private final ProductGroupCategoryMapper productGroupCategoryMapper;
    private final ProductGroupPageableMapper productGroupPageableMapper;


    @Override
    public ProductGroupFetchResponse fetchProductGroup(long productGroupId) {
        ProductGroupFetchResponse productGroupFetchResponse = productGroupFetchRepository.fetchProductGroup(productGroupId, SecurityUtils.currentSellerIdOpt())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_GROUP_NOT_FOUND_MSG));

        setCategories(productGroupFetchResponse);
        setProducts(productGroupFetchResponse);

        return productGroupFetchResponse;
    }

    @Override
    public List<Long> fetchIds() {
        return productGroupFetchRepository.fetchProductGroupIds();
    }

    @Override
    public List<ProductGroupInfoDto> fetchProductGroup(String externalProductUuId) {
        List<ProductGroupFetchResponse> productGroupFetchResponses = productGroupFetchRepository.fetchProductGroup(
            externalProductUuId);

        return productGroupFetchResponses.stream()
            .map(p -> new ProductGroupInfoDto(p.getProductGroupId(), p.getProductGroup().getProductGroupName()))
            .toList();
    }

    @Override
    public CustomPageable<ProductGroupDetailResponse> fetchProductGroups(ProductGroupFilter filter, Pageable pageable) {
        List<ProductGroupDetailResponse> productGroupDetailResponses;
        if (filter.isNoOffsetFetch()) {
            productGroupDetailResponses = fetchProductsWithNoOffset(filter, pageable);
        }else {
            productGroupDetailResponses = fetchProducts(filter, pageable);
        }

        long total = fetchProductGroupCount(filter);
        Set<Long> categoryIds = extractCategoryIdsFromProductGroups(productGroupDetailResponses);

        setCategories(productGroupDetailResponses, categoryIds);
        setProducts(productGroupDetailResponses);

        return productGroupPageableMapper.toProductGroupDetailResponse(productGroupDetailResponses, pageable, total);
    }

    @Override
    public ProductGroup fetchProductGroupEntity(long productGroupId) {
        return productGroupFetchRepository.fetchProductGroupEntity(productGroupId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_GROUP_NOT_FOUND_MSG));
    }

    @Override
    public List<ProductGroupDetailResponse> fetchProductGroups(Long offset, int size, boolean desc, List<Long> sellerIds) {
        List<ProductGroupDetailResponse> productGroupDetailResponses;
        if(desc){
            productGroupDetailResponses = productGroupFetchRepository.fetchProductsWithNoOffset(offset, size, sellerIds);
        }else{
            productGroupDetailResponses = productGroupFetchRepository.fetchProductsWithNoOffsetAsc(offset, size, sellerIds);
        }
        setProducts(productGroupDetailResponses);
        return productGroupDetailResponses;
    }

    @Override
    public List<ProductGroupDetailResponse> fetchProductGroups(List<Long> productGroupIds) {
        List<ProductGroupDetailResponse> productGroupDetailResponses = productGroupFetchRepository.fetchProductGroups(productGroupIds);
        setProducts(productGroupDetailResponses);
        return productGroupDetailResponses;
    }

    public List<ExternalContext> fetchExternalProducts(long siteId){
        return productGroupFetchRepository.fetchExternalProducts(siteId);
    }


    private List<ProductGroupDetailResponse> fetchProducts(ProductGroupFilter filter, Pageable pageable) {
        return productGroupFetchRepository.fetchProductGroups(filter, pageable);
    }

    private List<ProductGroupDetailResponse> fetchProductsWithNoOffset(ProductGroupFilter filter, Pageable pageable) {
        return productGroupFetchRepository.fetchProductsWithNoOffset(filter, pageable);
    }

    private void setCategories(ProductGroupFetchResponse productGroupFetchResponse){
        List<TreeCategoryContext> treeCategoryContexts = categoryFetchService.fetchAllParentCategories(productGroupFetchResponse.getProductGroup().getCategoryId());
        productGroupFetchResponse.setCategories(treeCategoryContexts);
    }


    private void setCategories(List<ProductGroupDetailResponse> productGroupDetailResponses, Set<Long> categoryIds){
        List<TreeCategoryContext> treeCategoryContexts = categoryFetchService.fetchAllCategories(categoryIds);
        productGroupCategoryMapper.setCategoryFullPath(productGroupDetailResponses, treeCategoryContexts);
    }


    private void setProducts(ProductGroupFetchResponse productGroupFetchResponse) {
        long productGroupId = productGroupFetchResponse.getProductGroup().getProductGroupId();
        Set<ProductFetchResponse> productFetchResponses = productFetchService.fetchProducts(Collections.singletonList(productGroupId));
        productGroupFetchResponse.setProducts(productFetchResponses);
    }

    private void setProducts(List<ProductGroupDetailResponse> productGroupDetailResponses) {

        List<Long> productGroupIds = extractProductGroupIds(productGroupDetailResponses);
        Set<ProductFetchResponse> productFetchResponses = productFetchService.fetchProducts(productGroupIds);

        Map<Long, Set<ProductFetchResponse>> productGroupIdMap = productFetchResponses.stream()
                .collect(Collectors.groupingBy(
                        ProductFetchResponse::getProductGroupId,
                        Collectors.toCollection(LinkedHashSet::new)
                ));

        productGroupDetailResponses.forEach(productGroupDetailResponse -> {
            Set<ProductFetchResponse> value = productGroupIdMap.getOrDefault(productGroupDetailResponse.getProductGroup().getProductGroupId(), Collections.emptySet());
            if(value.isEmpty()){
                Set<ProductFetchResponse> response = productFetchService.fetchProducts(productGroupDetailResponse.getProductGroup().getProductGroupId());
                productGroupDetailResponse.setProducts(response);
            }else{
                productGroupDetailResponse.setProducts(value);

            }
        });

    }


    private long fetchProductGroupCount(ProductGroupFilter filter) {
        Long total = productGroupFetchRepository.fetchProductGroupCountQuery(filter).fetchOne();
        if(total == null) return 0;
        return total;
    }


    private List<Long> extractProductGroupIds(List<ProductGroupDetailResponse> productGroupDetailResponses){
        return productGroupDetailResponses.stream()
                .map(ProductGroupDetailResponse::getProductGroup)
                .map(ProductGroupInfo::getProductGroupId)
                .toList();

    }


    private Set<Long> extractCategoryIdsFromProductGroups(List<ProductGroupDetailResponse> productGroupDetailResponses) {
        return productGroupDetailResponses.stream()
                .map(ProductGroupDetailResponse::getProductGroup)
                .map(ProductGroupInfo::getPath)
                .flatMap(path -> splitCategoryIds(path).stream())
                .collect(Collectors.toSet());
    }

    private List<Long> splitCategoryIds(String path) {
        return Arrays.stream(path.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }



}

package com.connectly.partnerAdmin.module.external.service.product;


import com.connectly.partnerAdmin.module.category.core.ExternalCategoryContext;
import com.connectly.partnerAdmin.module.category.service.CategoryFetchService;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.external.core.BaseInterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductInfoDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductMappingDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductSiteDto;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.filter.ExternalProductFilter;
import com.connectly.partnerAdmin.module.external.mapper.ExternalProductPageableMapper;
import com.connectly.partnerAdmin.module.external.repository.product.ExternalProductFetchRepository;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.service.stock.ProductFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ExternalProductFetchServiceImpl implements ExternalProductFetchService {

    private final ProductFetchService productFetchService;
    private final CategoryFetchService categoryFetchService;
    private final ExternalProductFetchRepository externalProductFetchRepository;
    private final ExternalProductPageableMapper externalProductPageableMapper;

    @Override
    public List<ExternalProduct> fetchExternalProductEntities(long productGroupId) {
        return externalProductFetchRepository.fetchExternalProductEntities(productGroupId);
    }

    @Override
    public List<ExternalProduct> fetchExternalProductEntities(long siteId, List<Long> productGroupIds) {
        return externalProductFetchRepository.fetchExternalProductEntities(siteId, productGroupIds);
    }

    @Override
    public List<ExternalProductSiteDto> fetchHasSyncExternalProducts(List<Long> productGroupIds, List<Long> siteIds){
        return externalProductFetchRepository.fetchHasSyncExternalProducts(productGroupIds, siteIds);
    }

    @Override
    public List<ExternalProductMappingDto> fetchHasExistingExternalProducts(List<String> externalIds, long siteId) {
        return externalProductFetchRepository.fetchHasExistingExternalProducts(externalIds, siteId);
    }

    @Override
    public List<BaseInterlockingProduct> fetchProductMapping(long sellerId){
        return externalProductFetchRepository.fetchProductMappings(sellerId);
    }

    @Override
    public Optional<ExternalProduct> fetchHasExternalProductEntity(long productGroupId, long siteId) {
        return externalProductFetchRepository.fetchHasExternalProductEntity(productGroupId, siteId);
    }

    @Override
    public CustomPageable<ExternalProductInfoDto> fetchExternalProducts(ExternalProductFilter filter, Pageable pageable) {
        List<ExternalProductInfoDto> externalProductInfos = externalProductFetchRepository.fetchExternalProductInfos(filter, pageable);
        if (externalProductInfos.isEmpty()) return externalProductPageableMapper.toExternalProductInfos(new ArrayList<>(), pageable, 0L);
        Set<Long> categoryIds = extractCategoryIdsFromProductGroups(externalProductInfos);
        setCategories(externalProductInfos, filter.getSiteId(), categoryIds);
        setProducts(externalProductInfos);

        return externalProductPageableMapper.toExternalProductInfos(externalProductInfos, pageable, 0L);
    }


    private void setCategories(List<ExternalProductInfoDto> externalProductInfos, long siteId, Set<Long> categoryIds) {
        List<ExternalCategoryContext> externalCategoryContexts = categoryFetchService.fetchExternalCategoriesByOurCategoryIds(siteId, categoryIds);

        Map<Long, ExternalCategoryContext> categoryContextMap = externalCategoryContexts.stream()
                .collect(Collectors.toMap(ExternalCategoryContext::getCategoryId, Function.identity(), (v1, v2) -> v1));

        externalProductInfos.removeIf(productInfo -> {
            if (productInfo.getCategoryMappingId() == null) {
                List<Long> longs = splitCategoryIds(productInfo.getCategoryPath());
                for (Long id : longs) {
                    ExternalCategoryContext externalCategoryContext = categoryContextMap.get(id);
                    if (externalCategoryContext != null) {
                        productInfo.setCategoryMapping(externalCategoryContext);
                        return false;
                    }
                }

                log.error(String.format("Category Mapping Failed Site %s, Category %d", SiteName.of(siteId).getName(), productInfo.getCategoryId()));
                return true;

            }
            return false;
        });
    }

    private void setProducts(List<ExternalProductInfoDto> externalProductInfos) {
        List<Long> productGroupIds = extractProductGroupIds(externalProductInfos);
        Set<ProductFetchResponse> productFetchResponses = productFetchService.fetchProducts(productGroupIds);

        Map<Long, Set<ProductFetchResponse>> productGroupIdMap = productFetchResponses.stream()
                .collect(Collectors.groupingBy(
                        ProductFetchResponse::getProductGroupId,
                        Collectors.toCollection(LinkedHashSet::new)
                ));

        externalProductInfos.removeIf(externalProductInfo -> {
            Set<ProductFetchResponse> value = productGroupIdMap.get(externalProductInfo.getProductGroupId());

            if (value == null) {
                return true;
            } else {
                externalProductInfo.setProducts(value);
                return false;
            }
        });
    }



    private Set<Long> extractCategoryIdsFromProductGroups(List<ExternalProductInfoDto> externalProductInfos) {
        return externalProductInfos.stream()
                .filter(e -> e.getCategoryMappingId() == null)
                .map(ExternalProductInfoDto::getCategoryPath)
                .flatMap(path -> splitCategoryIds(path).stream())
                .collect(Collectors.toSet());
    }



    private List<Long> splitCategoryIds(String path) {
        return Arrays.stream(path.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private List<Long> extractProductGroupIds(List<ExternalProductInfoDto> externalProductInfos){
        return externalProductInfos.stream()
                .map(ExternalProductInfoDto::getProductGroupId)
                .toList();

    }


}

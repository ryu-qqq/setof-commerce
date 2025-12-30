package com.connectly.partnerAdmin.module.external.service.product;

import com.connectly.partnerAdmin.module.external.core.BaseInterlockingProduct;
import com.connectly.partnerAdmin.module.external.core.InterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.SellerExternalIntegrationDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductMappingDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductSiteDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductUpdateDto;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.mapper.ExternalProductMapper;
import com.connectly.partnerAdmin.module.external.repository.product.ExternalProductRepository;
import com.connectly.partnerAdmin.module.external.service.ExternalSiteSellerRelationShipFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ExternalProductQueryServiceImpl implements ExternalProductQueryService {

    private final ExternalSiteSellerRelationShipFetchService externalSiteSellerRelationShipFetchService;
    private final ExternalProductMapper interLockingProductMapper;
    private final ExternalProductFetchService externalProductFetchService;
    private final ExternalProductRepository externalProductRepository;

    @Override
    public void productSync(long sellerId) {
        List<BaseInterlockingProduct> baseInterlockingProducts = externalProductFetchService.fetchProductMapping(sellerId);
        registrationSync(baseInterlockingProducts);
    }

    @Override
    public void registrationSync(List<? extends InterlockingProduct> interLockingProducts) {

        Set<Long> sellerIds = interLockingProducts.stream().map(InterlockingProduct::getSellerId).collect(Collectors.toSet());
        List<SellerExternalIntegrationDto> interLockingSites = externalSiteSellerRelationShipFetchService.fetchInterLockingSellers(sellerIds);
        List<ExternalProduct> externalProducts = interLockingProductMapper.toInterLockingProductMappings(interLockingProducts, interLockingSites);


        List<ExternalProductSiteDto> existingMappings = hasExistMappingHistory(externalProducts);
        Set<ExternalProductSiteDto> existingMappingsSet = new HashSet<>(existingMappings);

        List<ExternalProduct> newMappings = externalProducts.stream()
                .filter(mapping -> !existingMappingsSet.contains(new ExternalProductSiteDto(mapping.getProductGroupId(), mapping.getSiteId())))
                .collect(Collectors.toList());

        if (!newMappings.isEmpty()) {
            externalProductRepository.saveAll(newMappings);
        }
    }

    @Override
    public long updateMappingStatus(List<ExternalProductUpdateDto> externalProductUpdates, long siteId) {
        Map<Long, ExternalProductUpdateDto> productGroupIdMap = externalProductUpdates.stream().collect(Collectors.toMap(ExternalProductMappingDto::getProductGroupId, Function.identity(),
                (existingValue, newValue) -> existingValue));

        List<ExternalProduct> externalProducts = externalProductFetchService.fetchExternalProductEntities(siteId, new ArrayList<>(productGroupIdMap.keySet()));
        externalProducts.forEach(externalProduct -> {
            ExternalProductUpdateDto externalProductUpdateDto = productGroupIdMap.get(externalProduct.getProductGroupId());
            if(externalProductUpdateDto !=null){
                externalProduct.update(externalProductUpdateDto.getMappingStatus(), externalProductUpdateDto.getExternalIdx());
            }
        });

        return externalProductUpdates.size();
    }


    private List<ExternalProductSiteDto> hasExistMappingHistory(List<ExternalProduct> externalProducts){
        List<Long> siteIds = externalProducts.stream().map(ExternalProduct::getSiteId).toList();
        List<Long> productGroupIds = externalProducts.stream().map(ExternalProduct::getProductGroupId).collect(Collectors.toList());

        return externalProductFetchService.fetchHasSyncExternalProducts(productGroupIds, siteIds);
    }



}

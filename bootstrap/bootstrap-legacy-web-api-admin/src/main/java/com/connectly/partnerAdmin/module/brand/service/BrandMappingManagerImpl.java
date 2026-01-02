package com.connectly.partnerAdmin.module.brand.service;

import com.connectly.partnerAdmin.module.brand.core.BrandMappingInfo;
import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.core.ExternalBrandContext;
import com.connectly.partnerAdmin.module.brand.entity.BrandMapping;
import com.connectly.partnerAdmin.module.brand.repository.BrandMappingRepository;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class BrandMappingManagerImpl implements BrandMappingManager {

    private final BrandFetchService brandFetchService;
    private final BrandMappingRepository brandMappingRepository;

    @Override
    public void convertExternalBrandToInternalBrand(long siteId, List<BrandMappingInfo> brandMappingInfos) {

        Set<String> mappingBrandIdSet = brandMappingInfos.stream()
                .map(BrandMappingInfo::getBrandMappingId)
                .collect(Collectors.toSet());

        List<ExternalBrandContext> externalBrandContexts = brandFetchService.fetchExternalBrands(siteId, new ArrayList<>(mappingBrandIdSet));

        List<BrandMappingInfo> newBrandMappingInfos = new ArrayList<>();

        Map<String, ExternalBrandContext> brandIdMap = externalBrandContexts.stream()
                .collect(Collectors.toMap(ExternalBrandContext::getMappingBrandId, Function.identity(), (v1, v2) -> v1));

        for (BrandMappingInfo brandMappingInfo : brandMappingInfos) {
            ExternalBrandContext externalBrandContext = brandIdMap.get(brandMappingInfo.getBrandMappingId());
            if (externalBrandContext == null) {
                newBrandMappingInfos.add(brandMappingInfo);
            } else {
                long brandId = externalBrandContext.getBrandId();
                brandMappingInfo.setBrandId(brandId);
            }
        }

        if (!newBrandMappingInfos.isEmpty()) {
            List<BrandMapping> savedBrandMappings = saveNewBrandMappingInfo(siteId, newBrandMappingInfos);

            for (BrandMapping savedMapping : savedBrandMappings) {
                newBrandMappingInfos.stream()
                        .filter(b -> b.getBrandMappingId().equals(savedMapping.getMappingBrandId()))
                        .forEach(b -> b.setBrandId(savedMapping.getBrandId()));
            }
        }
    }


    private List<BrandMapping> saveNewBrandMappingInfo(long siteId, List<BrandMappingInfo> newBrandMappingInfos) {
        List<BrandMapping> brandMappings = newBrandMappingInfos.stream()
                .map(b -> {
                    Optional<ExtendedBrandContext> extendedBrandContextOpt = brandFetchService.fetchBrandWithName(b.getBrandName());

                    return extendedBrandContextOpt.map(extendedBrandContext ->
                            BrandMapping.builder()
                                    .brandId(extendedBrandContext.getBrandId())
                                    .mappingBrandId(b.getBrandMappingId())
                                    .siteId(siteId)
                                    .siteName(SiteName.of(siteId).getName())
                                    .build()
                    ).orElseGet(() -> {
                        log.error("Brand not found for brandName: " + b.getBrandName() + ", siteId: " + siteId);
                        return null;
                    });
                })
                .filter(Objects::nonNull)
                .toList();

        if (!brandMappings.isEmpty()) {
            return brandMappingRepository.saveAll(brandMappings);
        }

        return new ArrayList<>();
    }

}

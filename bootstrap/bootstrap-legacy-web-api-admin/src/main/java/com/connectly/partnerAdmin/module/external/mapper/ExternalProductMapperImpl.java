package com.connectly.partnerAdmin.module.external.mapper;

import com.connectly.partnerAdmin.module.external.core.InterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.SellerExternalIntegrationDto;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ExternalProductMapperImpl implements ExternalProductMapper {


    @Override
    public List<ExternalProduct> toInterLockingProductMappings(List<? extends InterlockingProduct> interLockingProducts, List<SellerExternalIntegrationDto> interLockingSites){
        Map<Long, SellerExternalIntegrationDto> sellerIdMap = toSellerIdMap(interLockingSites);

        return interLockingProducts.stream()
                .flatMap(interLockingProduct -> {
                    SellerExternalIntegrationDto interLockingSite = sellerIdMap.get(interLockingProduct.getSellerId());
                    if (interLockingSite == null) {
                        return Stream.empty();
                    }
                    return interLockingSite.getSiteIds().stream()
                            .map(aLong ->
                                    ExternalProduct.builder()
                                    .productGroupId(interLockingProduct.getProductGroupId())
                                    .siteId(aLong)
                                    .mappingStatus(MappingStatus.PENDING)
                                    .build()
                    );
                })
                .collect(Collectors.toList());
    }

    private Map<Long, SellerExternalIntegrationDto> toSellerIdMap(List<SellerExternalIntegrationDto> interLockingSites){
        return interLockingSites.stream().collect(Collectors.toMap(SellerExternalIntegrationDto::getSellerId,
                Function.identity(), (existingValue, newValue)
                        -> existingValue));
    }


}

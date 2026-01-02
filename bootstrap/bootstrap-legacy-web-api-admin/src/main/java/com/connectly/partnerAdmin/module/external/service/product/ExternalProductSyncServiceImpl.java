package com.connectly.partnerAdmin.module.external.service.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ExternalProductSyncServiceImpl implements ExternalProductSyncService {

    private final ExternalProductFetchService externalProductFetchService;

    @Override
    public void sync(long productGroupId) {
        List<ExternalProduct> externalProducts = externalProductFetchService.fetchExternalProductEntities(productGroupId);
        externalProducts.forEach(ExternalProduct::update);
    }

    @Override
    public void update(long productGroupId, MappingStatus mappingStatus) {
        List<ExternalProduct> externalProducts = externalProductFetchService.fetchExternalProductEntities(productGroupId);
        externalProducts.forEach(e -> e.update(mappingStatus));
    }

}

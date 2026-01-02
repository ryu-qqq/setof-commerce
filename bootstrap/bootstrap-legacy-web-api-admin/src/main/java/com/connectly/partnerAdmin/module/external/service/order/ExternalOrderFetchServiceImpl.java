package com.connectly.partnerAdmin.module.external.service.order;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.external.dto.order.ExternalOrderMappingDto;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallOrderNotFoundException;
import com.connectly.partnerAdmin.module.external.repository.order.ExternalOrderFetchRepository;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExternalOrderFetchServiceImpl implements ExternalOrderFetchService {

    private final ExternalOrderFetchRepository externalOrderFetchRepository;

    @Override
    public boolean doesHasSyncOrder(long siteId, long externalIdx) {
        return externalOrderFetchRepository.doesHasSyncOrder(siteId, externalIdx);
    }

    @Override
    public List<ExternalOrderMappingDto> doesHasSyncOrders(List<Long> orderIds) {
        return externalOrderFetchRepository.doesHasInterlockingOrders(orderIds);
    }

    @Override
    public long fetchOrderId(long siteId, long externalIdx) {
        return externalOrderFetchRepository.fetchOrderId(siteId, externalIdx)
                .orElseThrow(() -> new ExternalMallOrderNotFoundException(SiteName.of(siteId).getName(), externalIdx));
    }

    @Override
    public Optional<ExternalOrderMappingDto> fetchByOrderId(long orderId) {
        return externalOrderFetchRepository.fetchByOrderId(orderId);
    }

}

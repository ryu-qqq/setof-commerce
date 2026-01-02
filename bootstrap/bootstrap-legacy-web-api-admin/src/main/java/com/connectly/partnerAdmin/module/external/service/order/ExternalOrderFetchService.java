package com.connectly.partnerAdmin.module.external.service.order;

import java.util.List;
import java.util.Optional;

import com.connectly.partnerAdmin.module.external.dto.order.ExternalOrderMappingDto;

public interface ExternalOrderFetchService {
    boolean doesHasSyncOrder(long siteId, long externalIdx);

    List<ExternalOrderMappingDto> doesHasSyncOrders(List<Long> orderIds);

    long fetchOrderId(long siteId, long externalIdx);

    Optional<ExternalOrderMappingDto> fetchByOrderId(long orderId);

}

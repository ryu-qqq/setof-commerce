package com.connectly.partnerAdmin.module.external.repository.order;

import java.util.List;
import java.util.Optional;

import com.connectly.partnerAdmin.module.external.dto.order.ExternalOrderMappingDto;

public interface ExternalOrderFetchRepository {

    List<ExternalOrderMappingDto> doesHasInterlockingOrders(List<Long> orderIds);
    Optional<ExternalOrderMappingDto> fetchByOrderId(long orderId);
    boolean doesHasSyncOrder(long siteId, long externalIdx);
    Optional<Long> fetchOrderId(long siteId, long externalIdx);

}

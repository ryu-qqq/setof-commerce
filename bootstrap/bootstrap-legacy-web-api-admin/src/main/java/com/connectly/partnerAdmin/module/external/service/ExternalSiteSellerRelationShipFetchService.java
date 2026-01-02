package com.connectly.partnerAdmin.module.external.service;

import com.connectly.partnerAdmin.module.external.dto.SellerExternalIntegrationDto;

import java.util.List;
import java.util.Set;

public interface ExternalSiteSellerRelationShipFetchService {

    List<SellerExternalIntegrationDto> fetchInterLockingSellers(Set<Long> sellerIds);
}

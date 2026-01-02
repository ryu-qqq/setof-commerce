package com.connectly.partnerAdmin.module.external.repository.seller;

import com.connectly.partnerAdmin.module.external.dto.SellerExternalIntegrationDto;

import java.util.List;
import java.util.Set;

public interface InterlockingSellerFetchRepository {
    List<SellerExternalIntegrationDto> fetchInterLockingSites(Set<Long> sellerIds);
}

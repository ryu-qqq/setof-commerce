package com.connectly.partnerAdmin.module.external.service;


import com.connectly.partnerAdmin.module.external.dto.SellerExternalIntegrationDto;
import com.connectly.partnerAdmin.module.external.repository.seller.InterlockingSellerFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ExternalSiteSellerRelationShipFetchServiceImpl implements ExternalSiteSellerRelationShipFetchService {

    private final InterlockingSellerFetchRepository interLockingSellerFetchRepository;

    @Override
    public List<SellerExternalIntegrationDto> fetchInterLockingSellers(Set<Long> sellerIds){
        return interLockingSellerFetchRepository.fetchInterLockingSites(sellerIds);
    }
}

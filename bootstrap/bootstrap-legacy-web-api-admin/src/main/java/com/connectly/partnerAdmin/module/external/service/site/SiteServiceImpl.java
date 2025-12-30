package com.connectly.partnerAdmin.module.external.service.site;

import com.connectly.partnerAdmin.module.external.dto.SiteResponse;
import com.connectly.partnerAdmin.module.external.repository.site.SiteFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class SiteServiceImpl implements SiteService {

    private final SiteFetchRepository siteFetchRepository;

    @Override
    public List<SiteResponse> fetchSites() {
        return siteFetchRepository.fetchSites();
    }

    @Override
    public List<SiteResponse> fetchSitesBySellerId(long sellerId) {
        return siteFetchRepository.fetchSitesBySellerId(sellerId);
    }
}

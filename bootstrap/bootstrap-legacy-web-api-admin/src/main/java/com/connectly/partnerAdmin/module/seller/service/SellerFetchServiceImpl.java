package com.connectly.partnerAdmin.module.seller.service;

import com.connectly.partnerAdmin.auth.core.UserPrincipal;
import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.seller.core.BaseSellerContext;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import com.connectly.partnerAdmin.module.seller.core.SellerContext;
import com.connectly.partnerAdmin.module.seller.dto.BusinessValidation;
import com.connectly.partnerAdmin.module.seller.dto.SellerDetailResponse;
import com.connectly.partnerAdmin.module.seller.dto.SellerResponse;
import com.connectly.partnerAdmin.module.seller.exception.SellerNotFoundException;
import com.connectly.partnerAdmin.module.seller.filter.SellerFilter;
import com.connectly.partnerAdmin.module.seller.mapper.SellerPageableMapper;
import com.connectly.partnerAdmin.module.seller.repository.SellerFetchRepository;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class SellerFetchServiceImpl implements SellerFetchService {
    private final SellerFetchRepository sellerFetchRepository;
    private final SellerPageableMapper sellerPageableMapper;

    @Override
    public boolean fetchHasSellerExist(long sellerId) {
        return sellerFetchRepository.fetchHasSellerExist(sellerId);
    }

    @Override
    public SellerContext fetchAuthorizedSeller() {
        UserPrincipal principal = SecurityUtils.getAuthentication();
        return new BaseSellerContext(principal.sellerId(), principal.email(), principal.getPassword(), SecurityUtils.getAuthorization(), ApprovalStatus.APPROVED);
    }

    @Override
    public Optional<SellerContext> fetchSellerInfo(String email) {
       return sellerFetchRepository.fetchSellerInfo(email);
    }


    @Override
    public SellerDetailResponse fetchSellerDetail(long sellerId) {
        SellerDetailResponse sellerDetailResponse =  sellerFetchRepository.fetchSellerDetail(sellerId).orElseThrow(SellerNotFoundException::new);
        sellerDetailResponse.setFilteredSites();
        return sellerDetailResponse;
    }

    @Override
    public boolean fetchBusinessValidation(BusinessValidation businessValidation) {
        return sellerFetchRepository.fetchBusinessValidation(businessValidation);
    }

    @Override
    public List<BusinessSellerContext> fetchSellersBusinessInfo(List<Long> sellerIds) {
        return sellerFetchRepository.fetchSellersBusinessInfo(sellerIds);
    }

    @Override
    public CustomPageable<SellerResponse> fetchSellers(SellerFilter filter, Pageable pageable){
        List<SellerResponse> sellers = sellerFetchRepository.fetchSellers(filter, pageable);
        long totalCount = fetchSellerCountQuery(filter);
        return sellerPageableMapper.toSellerContext(sellers, pageable, totalCount);
    }

    private long fetchSellerCountQuery(SellerFilter filter) {
        JPAQuery<Long> countQuery = sellerFetchRepository.fetchSellerCountQuery(filter);
        Long totalCount = countQuery.fetchOne();
        if(totalCount == null) return 0L;
        return totalCount;
    }

}

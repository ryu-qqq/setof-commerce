package com.connectly.partnerAdmin.module.seller.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import com.connectly.partnerAdmin.module.seller.core.SellerContext;
import com.connectly.partnerAdmin.module.seller.dto.BusinessValidation;
import com.connectly.partnerAdmin.module.seller.dto.SellerDetailResponse;
import com.connectly.partnerAdmin.module.seller.dto.SellerResponse;
import com.connectly.partnerAdmin.module.seller.filter.SellerFilter;
import com.querydsl.jpa.impl.JPAQuery;

public interface SellerFetchRepository {

    boolean fetchHasSellerExist(long sellerId);
    Optional<SellerContext> fetchSellerInfo(String email);

    Optional<SellerDetailResponse> fetchSellerDetail(long sellerId);
    List<BusinessSellerContext> fetchSellersBusinessInfo(List<Long> sellerIds);

    List<SellerResponse> fetchSellers(SellerFilter filter, Pageable pageable);

    JPAQuery<Long> fetchSellerCountQuery(SellerFilter filter);
    boolean fetchBusinessValidation(BusinessValidation businessValidation);
}

package com.connectly.partnerAdmin.module.seller.service;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import com.connectly.partnerAdmin.module.seller.core.SellerContext;
import com.connectly.partnerAdmin.module.seller.dto.BusinessValidation;
import com.connectly.partnerAdmin.module.seller.dto.SellerDetailResponse;
import com.connectly.partnerAdmin.module.seller.dto.SellerResponse;
import com.connectly.partnerAdmin.module.seller.filter.SellerFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SellerFetchService {

    boolean fetchHasSellerExist(long sellerId);
    SellerContext fetchAuthorizedSeller();
    Optional<SellerContext> fetchSellerInfo(String email);
    SellerDetailResponse fetchSellerDetail(long sellerId);
    boolean fetchBusinessValidation(BusinessValidation businessValidation);
    List<BusinessSellerContext> fetchSellersBusinessInfo(List<Long> sellerIds);

    CustomPageable<SellerResponse> fetchSellers(SellerFilter filter, Pageable pageable);


}

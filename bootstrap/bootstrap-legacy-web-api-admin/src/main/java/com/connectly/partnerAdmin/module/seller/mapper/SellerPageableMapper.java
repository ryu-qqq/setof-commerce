package com.connectly.partnerAdmin.module.seller.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.seller.dto.SellerResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SellerPageableMapper {

    CustomPageable<SellerResponse> toSellerContext(List<SellerResponse> sellerContexts, Pageable pageable, long total);

}

package com.connectly.partnerAdmin.module.seller.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.seller.dto.SellerResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellerPageableMapperImpl implements SellerPageableMapper{
    @Override
    public CustomPageable<SellerResponse> toSellerContext(List<SellerResponse> sellerContexts, Pageable pageable, long total) {
        Long lastDomainId = sellerContexts.isEmpty() ? null : sellerContexts.getLast().getSellerId();
        return new CustomPageable<>(sellerContexts, pageable, total, lastDomainId);
    }
}

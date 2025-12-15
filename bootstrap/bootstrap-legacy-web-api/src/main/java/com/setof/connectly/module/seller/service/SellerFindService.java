package com.setof.connectly.module.seller.service;

import com.setof.connectly.module.seller.dto.SellerInfo;
import com.setof.connectly.module.seller.dto.SenderDto;
import java.util.List;

public interface SellerFindService {
    SellerInfo fetchSeller(long sellerId);

    List<SenderDto> fetchSenders(List<Long> sellerIds);
}

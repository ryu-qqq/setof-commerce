package com.setof.connectly.module.seller.repository;

import com.setof.connectly.module.seller.dto.SellerInfo;
import com.setof.connectly.module.seller.dto.SenderDto;
import java.util.List;
import java.util.Optional;

public interface SellerFindRepository {

    Optional<SellerInfo> fetchSeller(long sellerId);

    List<SenderDto> fetchSenders(List<Long> sellerIds);
}

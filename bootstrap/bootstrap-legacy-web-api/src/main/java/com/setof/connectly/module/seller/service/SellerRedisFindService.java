package com.setof.connectly.module.seller.service;

import com.setof.connectly.module.seller.dto.SellerInfo;
import java.util.Optional;

public interface SellerRedisFindService {

    Optional<SellerInfo> fetchSellerInRedis(long sellerId);
}

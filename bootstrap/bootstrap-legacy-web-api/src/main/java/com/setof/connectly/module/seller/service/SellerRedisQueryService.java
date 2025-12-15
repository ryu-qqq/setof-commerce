package com.setof.connectly.module.seller.service;

import com.setof.connectly.module.seller.dto.SellerInfo;

public interface SellerRedisQueryService {

    void saveSellerInRedis(SellerInfo sellerInfo);
}

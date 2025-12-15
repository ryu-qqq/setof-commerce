package com.setof.connectly.module.cart.service.query;

public interface CartCountRedisQueryService {

    void updateCartCountInCache(long userId);

    void insertCartCountInCache(long userId, long qty);
}

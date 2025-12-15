package com.setof.connectly.module.cart.service.fetch;

public interface CartCountRedisFindService {

    Long fetchCartCountInCache(long userId);
}

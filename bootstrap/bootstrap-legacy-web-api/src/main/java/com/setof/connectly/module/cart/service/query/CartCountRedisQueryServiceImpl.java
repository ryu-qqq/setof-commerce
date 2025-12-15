package com.setof.connectly.module.cart.service.query;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.cart.repository.fetch.CartFindRepository;
import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartCountRedisQueryServiceImpl extends AbstractRedisService
        implements CartCountRedisQueryService {
    private final CartFindRepository cartFindRepository;

    public CartCountRedisQueryServiceImpl(
            StringRedisTemplate redisTemplate, CartFindRepository cartFindRepository) {
        super(redisTemplate);
        this.cartFindRepository = cartFindRepository;
    }

    @Override
    public void updateCartCountInCache(long userId) {
        String key = generateKey(RedisKey.CART_COUNT, String.valueOf(userId));
        JPAQuery<Long> countQuery = cartFindRepository.fetchCartCountQuery(userId);
        long actualCount = countQuery.fetchCount();
        save(key, String.valueOf(actualCount));
    }

    @Override
    public void insertCartCountInCache(long userId, long qty) {
        String key = generateKey(RedisKey.CART_COUNT, String.valueOf(userId));
        save(key, String.valueOf(qty));
    }
}

package com.setof.connectly.module.product.service.stock;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.product.dto.stock.StockDto;
import com.setof.connectly.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductStockRedisQueryServiceImpl extends AbstractRedisService
        implements ProductStockRedisQueryService {

    public ProductStockRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveStockInCache(StockDto stockDto) {
        String key = generateKey(RedisKey.STOCK, String.valueOf(stockDto.getProductId()));
        String value = JsonUtils.toJson(stockDto);
        save(key, value, RedisKey.STOCK.getHourDuration());
    }
}

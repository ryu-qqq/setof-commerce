package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.product.dto.stock.StockDto;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductStockRedisFindServiceImpl extends AbstractRedisService
        implements ProductStockRedisFindService {

    public ProductStockRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Optional<StockDto> fetchStockInRedis(long productId) {
        String key = generateKey(RedisKey.STOCK, String.valueOf(productId));
        String value = getValue(key);
        if (StringUtils.hasText(value)) return parseStock(value);
        else return Optional.empty();
    }

    @Override
    public List<StockDto> fetchStockInRedis(List<Long> productIds) {

        List<String> keys =
                productIds.stream()
                        .map(productId -> generateKey(RedisKey.STOCK, String.valueOf(productId)))
                        .collect(Collectors.toList());

        List<String> values = getValues(keys);

        if (values.isEmpty()) return new ArrayList<>();

        return values.stream()
                .filter(StringUtils::hasText)
                .map(this::parseStock)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<StockDto> parseStock(String value) {
        StockDto stockDto = JsonUtils.fromJson(value, StockDto.class);
        return Optional.of(stockDto);
    }
}

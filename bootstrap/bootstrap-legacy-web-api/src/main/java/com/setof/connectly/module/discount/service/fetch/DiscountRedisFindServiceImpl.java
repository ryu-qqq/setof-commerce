package com.setof.connectly.module.discount.service.fetch;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.dto.DiscountRedisFetchDto;
import com.setof.connectly.module.discount.enums.IssueType;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DiscountRedisFindServiceImpl extends AbstractRedisService
        implements DiscountRedisFindService {

    public DiscountRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Optional<DiscountCacheDto> getHighestPriorityDiscount(
            DiscountRedisFetchDto discountRedisFetchDto) {

        List<String> keys =
                generateKeys(
                        discountRedisFetchDto.getProductGroupId(),
                        discountRedisFetchDto.getSellerId());
        List<String> values = getValues(keys);

        return values.stream().filter(Objects::nonNull).findFirst().map(this::parseDiscount);
    }

    @Override
    public List<DiscountCacheDto> getHighestPriorityDiscounts(
            List<DiscountRedisFetchDto> discountRedisFetches) {

        List<String> keys = new ArrayList<>();
        for (DiscountRedisFetchDto dto : discountRedisFetches) {
            keys.addAll(generateKeys(dto.getProductGroupId(), dto.getSellerId()));
        }

        List<String> values = getValues(keys);

        return values.stream()
                .filter(Objects::nonNull)
                .map(this::parseDiscount)
                .collect(Collectors.toList());
    }

    private List<String> generateKeys(String key1, String key2) {
        return Arrays.asList(
                generateKey(RedisKey.DISCOUNT, IssueType.PRODUCT.getName() + key1),
                generateKey(RedisKey.DISCOUNT, IssueType.SELLER.getName() + key2));
    }

    private DiscountCacheDto parseDiscount(String value) {
        return JsonUtils.fromJson(value, DiscountCacheDto.class);
    }
}

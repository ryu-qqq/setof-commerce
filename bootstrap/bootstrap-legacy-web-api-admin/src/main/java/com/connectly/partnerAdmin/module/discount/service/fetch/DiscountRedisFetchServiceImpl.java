package com.connectly.partnerAdmin.module.discount.service.fetch;

import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisFetchService;
import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountRedisFetchServiceImpl extends AbstractRedisFetchService implements DiscountRedisFetchService {

    public DiscountRedisFetchServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Optional<BaseDiscountInfo> getHighestPriorityDiscount(PriceHolder priceHolder) {
        List<String> keys = generateKeys(priceHolder.getProductGroupId(), priceHolder.getSellerId());
        List<String> values = getValues(keys);

        return values.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .flatMap(this::parseJson);
    }

    @Override
    public List<BaseDiscountInfo> getHighestPriorityDiscounts(List<? extends PriceHolder> priceHolders) {
        List<String> keys = new ArrayList<>();
        for (PriceHolder priceHolder : priceHolders) {
            keys.addAll(generateKeys(priceHolder.getProductGroupId(), priceHolder.getSellerId()));
        }

        List<String> values = getValues(keys);
        return values.stream()
                .filter(Objects::nonNull)
                .map(this::parseJson)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }


    private Optional<BaseDiscountInfo> parseJson(String value) {
        return super.parseJson(value, BaseDiscountInfo.class);
    }

    private List<String> generateKeys(long key1, long key2) {
        return Arrays.asList(
                generateKey(RedisKey.DISCOUNT, IssueType.PRODUCT.getName() + key1),
                generateKey(RedisKey.DISCOUNT, IssueType.SELLER.getName() + key2)
        );
    }


}

package com.setof.connectly.module.product.service.group.query;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductGroupRedisQueryServiceImpl extends AbstractRedisService
        implements ProductGroupRedisQueryService {

    public ProductGroupRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveSellerProductGroupThumbnail(
            long sellerId, List<ProductGroupThumbnail> productGroupThumbnails) {
        String key = generateKey(RedisKey.PRODUCT_SELLER, String.valueOf(sellerId));
        String value = JsonUtils.toJson(productGroupThumbnails);
        save(key, value, RedisKey.PRODUCT_SELLER.getHourDuration());
    }

    @Override
    public void saveBrandProductGroupThumbnail(
            long brandId, List<ProductGroupThumbnail> productGroupThumbnails) {
        String key = generateKey(RedisKey.PRODUCT_BRAND, String.valueOf(brandId));
        String value = JsonUtils.toJson(productGroupThumbnails);
        save(key, value, RedisKey.PRODUCT_BRAND.getHourDuration());
    }
}

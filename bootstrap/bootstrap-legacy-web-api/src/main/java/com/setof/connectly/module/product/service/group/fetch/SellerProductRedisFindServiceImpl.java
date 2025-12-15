package com.setof.connectly.module.product.service.group.fetch;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class SellerProductRedisFindServiceImpl extends AbstractRedisService
        implements SellerProductRedisFindService {

    public SellerProductRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public List<ProductGroupThumbnail> fetchProductGroupWithSeller(long sellerId) {
        String key = generateKey(RedisKey.PRODUCT_SELLER, String.valueOf(sellerId));
        String data = getValue(key);
        if (StringUtils.hasText(data)) return parseProductGroupThumbnail(data);
        return new ArrayList<>();
    }

    private List<ProductGroupThumbnail> parseProductGroupThumbnail(String data) {
        return JsonUtils.fromJsonList(data, ProductGroupThumbnail.class);
    }
}

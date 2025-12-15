package com.setof.connectly.module.product.service.individual.query;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.product.dto.stock.ProductStatusUpdateEvent;
import com.setof.connectly.module.product.repository.individual.ProductJdbcRepository;
import com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductQueryServiceImpl extends AbstractRedisService implements ProductQueryService {
    private final ProductJdbcRepository productJdbcRepository;
    private final ProductGroupFindService productGroupFindService;

    public ProductQueryServiceImpl(
            StringRedisTemplate redisTemplate,
            ProductJdbcRepository productJdbcRepository,
            ProductGroupFindService productGroupFindService) {
        super(redisTemplate);
        this.productJdbcRepository = productJdbcRepository;
        this.productGroupFindService = productGroupFindService;
    }

    @Override
    public void updatesStatus(List<Long> productIds) {
        List<Long> productGroupIds = productGroupFindService.fetchProductGroupIds(productIds);
        productJdbcRepository.updateProductGroupSoldOutStatus(productGroupIds);
    }

    @Override
    public void rollBackUpdatesStatus(List<Long> productIds) {
        List<Long> productGroupIds = productGroupFindService.fetchProductGroupIds(productIds);
        productJdbcRepository.updateProductGroupAvailableStatus(productGroupIds);
    }

    @EventListener
    public void onProductStatusUpdate(ProductStatusUpdateEvent event) {
        event.getProductIds()
                .forEach(
                        aLong -> {
                            String key = generateKey(RedisKey.SOLD_OUT, String.valueOf(aLong));
                            long userId = SecurityUtils.currentUserId();
                            save(key, String.valueOf(userId));
                        });
    }
}

package com.setof.connectly.module.category.service;

import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CategoryRedisQueryServiceImpl extends AbstractRedisService
        implements CategoryRedisQueryService {

    public CategoryRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void saveByIssueTypeAndTargetId(List<CategoryDisplayDto> categoryDisplays) {
        String key = generateKey(RedisKey.CATEGORIES, "");
        String value = JsonUtils.toJson(categoryDisplays);
        save(key, value);
    }
}

package com.setof.connectly.module.category.service;

import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoryRedisFindServiceImpl extends AbstractRedisService
        implements CategoryRedisFindService {

    public CategoryRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public List<CategoryDisplayDto> fetchAllCategoriesAsTree() {
        String key = generateKey(RedisKey.CATEGORIES, "");
        String value = getValue(key);
        if (StringUtils.hasText(value)) return parseCategories(value);
        return new ArrayList<>();
    }

    private List<CategoryDisplayDto> parseCategories(String value) {
        return JsonUtils.fromJsonList(value, CategoryDisplayDto.class);
    }
}

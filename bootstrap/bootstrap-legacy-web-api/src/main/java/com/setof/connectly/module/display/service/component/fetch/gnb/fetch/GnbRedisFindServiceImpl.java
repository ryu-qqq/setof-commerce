package com.setof.connectly.module.display.service.component.fetch.gnb.fetch;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GnbRedisFindServiceImpl extends AbstractRedisService implements GnbRedisFindService {

    public GnbRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public List<GnbResponse> fetchGnbs() {
        String key = generateKey(RedisKey.GNBS, "");
        String value = getValue(key);
        if (StringUtils.hasText(value)) return parseGnbs(value);
        return new ArrayList<>();
    }

    private List<GnbResponse> parseGnbs(String data) {
        return JsonUtils.fromJsonList(data, GnbResponse.class);
    }
}

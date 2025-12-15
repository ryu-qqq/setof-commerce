package com.setof.connectly.module.display.service.component.fetch.gnb;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import com.setof.connectly.module.utils.JsonUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GnbRedisQueryServiceImpl extends AbstractRedisService implements GnbRedisQueryService {
    public GnbRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveGnbsInRedis(List<GnbResponse> gnbResponses) {
        String value = JsonUtils.toJson(gnbResponses);
        LocalDateTime now = LocalDateTime.now();

        Optional<GnbResponse> earliestEndDateGnb = getEarliestEndDateGnb(gnbResponses);
        if (earliestEndDateGnb.isPresent()) {
            GnbResponse gnbResponse = earliestEndDateGnb.get();
            String key = generateKey(RedisKey.GNBS, "");
            Duration ttl =
                    Duration.between(now, gnbResponse.getDisplayPeriod().getDisplayEndDate());
            save(key, value, ttl);
        }
    }

    private Optional<GnbResponse> getEarliestEndDateGnb(List<GnbResponse> gnbResponses) {
        return gnbResponses.stream()
                .min(
                        Comparator.comparing(
                                gnbResponse -> gnbResponse.getDisplayPeriod().getDisplayEndDate()));
    }
}

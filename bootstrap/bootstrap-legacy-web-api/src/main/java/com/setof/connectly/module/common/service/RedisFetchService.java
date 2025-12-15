package com.setof.connectly.module.common.service;

import java.util.List;
import org.springframework.data.redis.core.ValueOperations;

public interface RedisFetchService {

    String getValue(String key);

    List<String> getValues(List<String> keys);

    ValueOperations<String, String> opsValue();
}

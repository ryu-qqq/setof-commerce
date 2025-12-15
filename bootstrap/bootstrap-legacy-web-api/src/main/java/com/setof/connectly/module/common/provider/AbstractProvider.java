package com.setof.connectly.module.common.provider;

import com.setof.connectly.module.exception.common.ProviderMappingException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProvider<K, T> implements Provider<K, T> {
    protected final Map<K, T> map = new HashMap<>();

    @Override
    public T get(K key) {
        T value = map.get(key);
        if (value == null) {
            throw new ProviderMappingException("No mapping found for key: " + key);
        }
        return value;
    }

    @Override
    public <R extends T> R get(K key, Class<R> clazz) {
        return null;
    }
}

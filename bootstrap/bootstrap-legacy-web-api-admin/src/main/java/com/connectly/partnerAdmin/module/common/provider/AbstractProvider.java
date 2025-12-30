package com.connectly.partnerAdmin.module.common.provider;

import com.connectly.partnerAdmin.module.common.exception.ProviderMappingException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProvider<K, T> implements Provider<K, T> {
    protected final Map<K, T> map = new HashMap<>();

    @Override
    public T get(K key) {
        T value = map.get(key);
        if (value == null) {
            throw new ProviderMappingException(key.toString());
        }
        return value;
    }

    @Override
    public <R extends T> R get(K key, Class<R> clazz) {
        return null;
    }

}

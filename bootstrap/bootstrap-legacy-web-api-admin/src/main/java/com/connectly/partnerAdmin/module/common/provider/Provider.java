package com.connectly.partnerAdmin.module.common.provider;

public interface Provider<K, T> {
    T get(K key);

    <R extends T> R get(K key, Class<R> clazz);

}

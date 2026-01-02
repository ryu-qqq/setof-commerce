package com.setof.connectly.infra.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
@Profile({"prod", "dev"})
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    private final ObjectMapper objectMapper;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        if (redisPassword != null && !redisPassword.isEmpty()) {
            redisConfig.setPassword(RedisPassword.of(redisPassword));
        }
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues() // null value 캐시안함
                        .computePrefixWith(CacheKeyPrefix.simple())
                        .entryTtl(Duration.ZERO) // 무제한 유효시간 설정
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new StringRedisSerializer()))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();

        cacheConfiguration.put(
                CacheKey.PRODUCTS,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(CacheKey.DEFAULT_EXPIRE_MINUTE)));

        cacheConfiguration.put(
                CacheKey.CATEGORIES,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.DEFAULT_EXPIRE_HOUR)));

        cacheConfiguration.put(
                CacheKey.GNBS,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.DEFAULT_EXPIRE_HOUR)));

        cacheConfiguration.put(
                CacheKey.BANNERS,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.DEFAULT_EXPIRE_HOUR)));

        cacheConfiguration.put(
                CacheKey.JOINED_USERS,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.TWO_EXPIRE_HOUR)));

        cacheConfiguration.put(
                CacheKey.PRODUCTS_BRAND,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.SIX_EXPIRE_HOUR)));

        cacheConfiguration.put(
                CacheKey.PRODUCTS_SELLER,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.SIX_EXPIRE_HOUR)));

        cacheConfiguration.put(
                CacheKey.USER_MILEAGE,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.ONE_EXPIRE_MINUTE)));

        cacheConfiguration.put(
                CacheKey.CART_COUNT,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(CacheKey.DEFAULT_EXPIRE_MINUTE)));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
                        redisConnectionFactory)
                .cacheDefaults(configuration)
                .withInitialCacheConfigurations(cacheConfiguration)
                .build();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(
                messageListenerAdapter, new PatternTopic("__keyevent@*__:expired"));
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter();
    }
}

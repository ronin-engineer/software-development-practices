package dev.ronin_engineer.software_development.infrastructure.config;

import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties(RedisCacheConfigurationProperties.class)
@ConditionalOnProperty(value = "app.cache.redis.enable", havingValue = "true")
public class RedisCacheConfig {

    @Value("${application-short-name}")
    private String applicationShortName;

    private RedisCacheConfiguration createCacheConfiguration(long timeoutInSeconds) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
                .fromSerializer(jdkSerializer);

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(pair)
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> {
                    String prefix = applicationShortName + "::";
                    if (StringUtils.hasText(cacheName)) {
                        prefix += cacheName + "::";
                    }
                    return prefix;
                })
                .entryTtl(Duration.ofSeconds(timeoutInSeconds));
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(
            RedisCacheConfigurationProperties properties) {

        // https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#reference
        log.info("Redis (/Lettuce) configuration enabled. With cache timeout " + properties
                .getTimeoutSeconds() + " seconds.");
        if (!CollectionUtils.isEmpty(properties.getNodes())) {
            log.info("Redis Cluster: {}", properties.getNodes());
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(
                    properties.getNodes());
            if (StringUtils.hasText(properties.getPassword())) {
                redisClusterConfiguration.setPassword(RedisPassword.of(properties.getPassword()));
            }

            // Cấu hình read master
            LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.UPSTREAM)
                .build();

            return new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
//            return new LettuceConnectionFactory(redisClusterConfiguration);
        } else {
            log.info("Redis Standalone: {}:{}", properties.getHost(), properties.getPort());
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(properties.getHost());
            redisStandaloneConfiguration.setPort(properties.getPort());
            if (StringUtils.hasText(properties.getPassword())) {
                log.info("Set password Redis");
                redisStandaloneConfiguration.setPassword(RedisPassword.of(properties.getPassword()));
            }
            return new LettuceConnectionFactory(redisStandaloneConfiguration);
        }
    }

    @Bean
    public RedisTemplate<Byte[], Byte[]> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<Byte[], Byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        log.info("Redis is connect success !!!");
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateWithStringKey(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration(RedisCacheConfigurationProperties properties) {
        return createCacheConfiguration(properties.getTimeoutSeconds());
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                          RedisCacheConfigurationProperties properties) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        for (Map.Entry<String, Long> cacheNameAndTimeout : properties.getCacheExpirations()
                .entrySet()) {
            cacheConfigurations.put(cacheNameAndTimeout.getKey(),
                    createCacheConfiguration(cacheNameAndTimeout.getValue()));
        }
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration(properties))
                .withInitialCacheConfigurations(cacheConfigurations).build();
    }
}
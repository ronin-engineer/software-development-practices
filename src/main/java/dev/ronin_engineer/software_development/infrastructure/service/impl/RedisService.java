package dev.ronin_engineer.software_development.infrastructure.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ronin_engineer.software_development.infrastructure.constant.DefaultValue;
import dev.ronin_engineer.software_development.infrastructure.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Configuration
public class RedisService implements CacheService {

    private final static long MAX_RETRY_TIMEOUT = 5 * 60 * 1000L;
    private final static long INIT_INTERVAL = 50L;

    private final static long MULTIPLIER = 2;


    @Autowired
    @Qualifier("redisTemplateWithStringKey")
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${application-name}")
    private String applicationShortName;



    @Override
    public void set(String key, int value) {
        set(key, String.valueOf(value));
    }

    @Override
    public void set(String key, String value) {
        key = generateKey(key);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long milliseconds) {
        key = generateKey(key);
        redisTemplate.opsForValue().set(key, value, milliseconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public Object get(String key) {
        key = generateKey(key);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        key = generateKey(key);
        redisTemplate.delete(key);
    }

    @Override
    public boolean lock(String key, long ttlMillisecond) {
        key = generateKey(key);
        // SETNX key 1 EX ttl
        return redisTemplate.opsForValue().setIfAbsent(key, DefaultValue.NUMBER_ONE, ttlMillisecond, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean lockWithRetry(String key, long keyTTL, long retryTimeout) {
        if (retryTimeout > MAX_RETRY_TIMEOUT)
            retryTimeout = MAX_RETRY_TIMEOUT;

        key = generateKey(key);
        long wait, totalWait = 0;
        int retries = 0;

        try {
            do {
                if (setNx(key, keyTTL)) { // call redis here
                    return true;
                }
                wait = INIT_INTERVAL * (long) Math.pow(MULTIPLIER, retries); // base x ( multiplier ^ retries)
                Thread.sleep(wait);
                retries++;
                totalWait += wait;
            } while (totalWait <= retryTimeout);

            log.error("Failed to lock key: " + key);
            return false;
        }
        catch (Exception e) {
            log.error("Exception at lock key with retry: " + key + e);
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        key = generateKey(key);
        redisTemplate.delete(key);
    }

    private boolean setNx(String key, long ttl) {
        return redisTemplate.opsForValue().setIfAbsent(key, DefaultValue.NUMBER_ONE, ttl, TimeUnit.MILLISECONDS);
    }

    private String generateKey(String key) {
        return applicationShortName + "::kv::" + key;
    }
}

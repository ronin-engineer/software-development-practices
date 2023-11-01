package dev.ronin_engineer.software_development.infrastructure.service;

public interface CacheService {

    void set(String key, int value);

    void set(String key, String value);

    void set(String key, String value, long ttl);

    Object get(String key);

    void delete(String key);

    boolean lock(String key, long ttl);

    boolean lockWithRetry(String key, long keyTTL, long retryTimeout);

    void unlock(String key);
}

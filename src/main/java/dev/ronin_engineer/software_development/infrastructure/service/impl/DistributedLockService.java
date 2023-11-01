package dev.ronin_engineer.software_development.infrastructure.service.impl;

import dev.ronin_engineer.software_development.infrastructure.service.LockService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Service
@Slf4j
public class DistributedLockService implements LockService {

    private static long lockTTL = 10000;

    private static long lockTimeout = 300000;

    @Value("${app.transaction.lock.ttl}")
    private long lockTTLInSecond;

    @Value("${app.transaction.lock.timeout}")
    private long lockTimeoutInSecond;

    @Autowired
    RedisService redisService;

    @Autowired
    @Qualifier("cacheThreadPool")
    Executor executor;


    @PostConstruct
    public void init() {
        lockTTL = lockTTLInSecond * 1000;
        lockTimeout = lockTimeoutInSecond * 1000;
    }

    @Override
    public boolean lock(String lockKey) {
        return redisService.lock(lockKey, lockTTL * 1000);
    }

    @Override
    public boolean lockWithRetry(List<String> lockKeys) {
        Map<String, CompletableFuture<Boolean>> lockFutureMap = new HashMap<>();

        try {
            for (String lockKey: lockKeys) {
                if (lockKey == null) {
                    continue;
                }

                lockFutureMap.put(lockKey,
                        CompletableFuture.supplyAsync(
                                () -> redisService.lockWithRetry(lockKey, lockTTL, lockTimeout),
                                executor
                        ));
            }

            CompletableFuture<Void> doneFuture = CompletableFuture.allOf(lockFutureMap.values().toArray(new CompletableFuture[lockFutureMap.values().size()]));
            CompletableFuture<Map<String, Boolean>> allResults = doneFuture.thenApply(unused -> {
                Map<String, Boolean> result = new HashMap<>();
                for (String lockKey : lockFutureMap.keySet()) {
                    CompletableFuture<Boolean> lockFuture = lockFutureMap.get(lockKey);
                    result.put(lockKey, lockFuture.join());
                }
                return result;
            });

            Map<String, Boolean> lockResultList = allResults.get();
            boolean lockResult = lockResultList.values().stream().allMatch(aBoolean -> aBoolean);
            if (!lockResult) {
                log.warn("Failed to distributed lock: " + lockKeys);
                unlock(lockResultList.keySet());
            }

            return lockResult;
        }
        catch (Exception e) {
            log.error("Exception at distributed lock with retry: " + lockKeys + ", message: ", e);
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        if (lockKey == null) {
            return;
        }

        CompletableFuture.runAsync(() -> redisService.unlock(lockKey), executor);
    }

    @Override
    public void unlock(Collection<String> lockKeys) {
        for (String key: lockKeys) {
            unlock(key);
        }
    }
}

package dev.ronin_engineer.software_development.infrastructure.service;

import java.util.Collection;
import java.util.List;

public interface LockService {

    boolean lock(String lockKey);

    boolean lockWithRetry(List<String> lockKeys);

    void unlock(String lockKey);

    void unlock(Collection<String> lockKeys);
}

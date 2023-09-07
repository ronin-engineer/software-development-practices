package dev.ronin_engineer.software_development.infrastructure.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "localCache", cacheManager = "localCacheManager")
public class LocalCacheService {

}

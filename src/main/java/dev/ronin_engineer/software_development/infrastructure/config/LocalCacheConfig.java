package dev.ronin_engineer.software_development.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("squid:S3740")
@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties(LocalCacheConfigurationProperties.class)
@ConditionalOnProperty(value = "app.cache.local.enable", havingValue = "true", matchIfMissing = true)
public class LocalCacheConfig {

  @Bean
  public Caffeine caffeineConfig(LocalCacheConfigurationProperties properties) {
    return Caffeine.newBuilder().expireAfterWrite(properties.getTimeoutSeconds(), TimeUnit.SECONDS);
  }

  @Bean
  @Primary
  public CacheManager localCacheManager(Caffeine caffeineCacheBuilder) {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCacheNames(Arrays.asList("localCache"));
    log.info("Configuring local cache manager");
    log.info("Using caffeine: {}", caffeineCacheBuilder.toString());

    cacheManager.setCaffeine(caffeineCacheBuilder);
    return cacheManager;
  }
}

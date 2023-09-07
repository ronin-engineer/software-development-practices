package dev.ronin_engineer.software_development.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cache.local")
@Data
public class LocalCacheConfigurationProperties {
    private long timeoutSeconds = 60;
}

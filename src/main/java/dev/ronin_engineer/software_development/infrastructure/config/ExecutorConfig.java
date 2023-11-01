package dev.ronin_engineer.software_development.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
public class ExecutorConfig {

  @Bean("cacheThreadPool")
  public Executor getExecutor() {
    return Executors.newCachedThreadPool();
  }

}
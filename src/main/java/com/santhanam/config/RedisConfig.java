package com.santhanam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redis configuration for the application.
 * Author: Santhanam
 */
@Configuration
@EnableRedisRepositories(basePackages = "com.santhanam.repository")
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    public RedisConfig(@Value("${spring.data.redis.host}") String redisHost, @Value("${spring.data.redis.port}") int redisPort) {
        log.info("[DEBUG] RedisConfig: Using Redis host: {} port: {}", redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
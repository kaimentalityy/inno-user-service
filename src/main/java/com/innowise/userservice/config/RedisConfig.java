package com.innowise.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * Configuration class for Redis caching setup.
 * Enables and configures Redis as a caching mechanism for the application.
 * Provides custom serialization configuration for Redis cache to handle Java 8 time types.
 *
 * @author Your Name
 * @version 1.0
 * @since 2024
 *
 * @see org.springframework.cache.annotation.EnableCaching
 * @see org.springframework.data.redis.cache.RedisCacheConfiguration
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Configures Redis cache with custom serialization settings.
     * Creates a RedisCacheConfiguration that uses Jackson JSON serialization
     * with support for Java 8 time types (LocalDate, LocalDateTime, etc.).
     * This ensures proper serialization/deserialization of temporal objects
     * when storing and retrieving from Redis cache.
     *
     * @return RedisCacheConfiguration configured with JSON serialization
     *         and Java 8 time module support
     *
     * @see ObjectMapper
     * @see JavaTimeModule
     * @see GenericJackson2JsonRedisSerializer
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );
    }

}
package com.innowise.userservice.config;

import com.innowise.userservice.model.dto.CardInfoDto;
import com.innowise.userservice.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

class RedisConfigTest {

    private final RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
    private final RedisConfig redisConfig = new RedisConfig();

    @Test
    void testObjectMapperBean() {
        assertNotNull(redisConfig.userDtoObjectMapper());
    }

    @Test
    void testRedisCacheManagerBean() {
        RedisCacheManager manager = redisConfig.cacheManager(connectionFactory, redisConfig.userDtoObjectMapper());
        assertNotNull(manager);
    }

    @Test
    void testRedisTemplateBean() {
        RedisTemplate<String, Object> template = redisConfig.redisTemplate(connectionFactory, redisConfig.userDtoObjectMapper());
        assertNotNull(template);
    }
}

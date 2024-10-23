package com.upwork.tinyurl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.tinyurl.model.UrlEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Value("${redis.host-name}")
    private String redisHostName;

    @Value("${redis.port}")
    private Integer redisPort;

    // Setting up the redis template object.
    @Bean
    public RedisTemplate<String, UrlEntity> redisTemplate() {
        Jackson2JsonRedisSerializer<UrlEntity> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(UrlEntity.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        RedisTemplate<String, UrlEntity> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }
}
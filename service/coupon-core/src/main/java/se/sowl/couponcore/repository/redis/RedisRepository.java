package se.sowl.couponcore.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {
    private final StringRedisTemplate redisTemplate;

    public RedisRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void rPush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public String lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public long lSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0L : size;
    }
}

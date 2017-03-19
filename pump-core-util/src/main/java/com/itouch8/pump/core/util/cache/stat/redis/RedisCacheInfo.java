package com.itouch8.pump.core.util.cache.stat.redis;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;

import com.itouch8.pump.core.util.cache.stat.base.CacheInfoSupport;


public class RedisCacheInfo extends CacheInfoSupport {

    public RedisCacheInfo(Cache cache) {
        StringRedisTemplate rt = (StringRedisTemplate) cache.getNativeCache();
        this.cacheName = cache.getName();
        this.cacheType = "Redis";

        String zSetKeys = cacheName + "~keys";
        @SuppressWarnings("unchecked")
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) rt.getKeySerializer();
        if (null == keySerializer) {
            this.keys = new LinkedHashSet<Object>();
            ZSetOperations<String, String> zSet = rt.opsForZSet();
            this.size = zSet.size(zSetKeys).intValue();
        } else {
            final byte[] rawKey = keySerializer.serialize(zSetKeys);
            Set<byte[]> rawValues = rt.execute(new RedisCallback<Set<byte[]>>() {
                public Set<byte[]> doInRedis(RedisConnection connection) {
                    return connection.zRangeByScore(rawKey, -1, 1);
                }
            }, true);
            Set<String> set = SerializationUtils.deserialize(rawValues, keySerializer);
            this.keys = new LinkedHashSet<Object>(set);
            this.size = this.keys.size();
        }
    }
}

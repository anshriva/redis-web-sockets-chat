package com.anubhav.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class Publisher {

    private JedisPool jedisPool;

    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    public Publisher(){
        this.jedisPool = new JedisPool(new JedisPoolConfig(),
                "localhost",
                6379,
                30000
        );
    }

    public void publish(String channel, String message){
        try (Jedis jedis = this.jedisPool.getResource()) {
            logger.info("going to publish the message to channel {} and message = {}", channel, message);
            jedis.publish(channel, message);
        }
    }
}

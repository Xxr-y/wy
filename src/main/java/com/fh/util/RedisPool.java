package com.fh.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static JedisPool jedisPool;

    private static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMinIdle(100);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
    }


    //静态块,只调用这个方法一次
    static {
        initPool();
    }


    //提供一个公共方法供外界使用
    public static Jedis getRedisPool(){
        return jedisPool.getResource();
    }


}

package it.home.travel.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;
/**
 * Jedis工具类:提供获取Jedis对象的方法
 */
public class JedisUtil {
    public static JedisPool jp;
    static {
        Properties pro = new Properties();
        try {
            pro.load(JedisUtil.class.getClassLoader().getResourceAsStream("jedis.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));
        config.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        jp = new JedisPool(config,pro.getProperty("host"),Integer.parseInt(pro.getProperty("port")));

    }
    public Jedis getJedis(){return jp.getResource();}
}

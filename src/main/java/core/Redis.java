package core;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Redis {
    public static void main(String[] args) {
        System.out.println(deleteKey("hkey"));
        System.out.println(getKey("hkey"));
    }

    private static Jedis getConnect() {
        return new JedisPool("localhost", 6379).getResource();
    }

    private static long deleteKey(String key) {
        return getConnect().del(key);
    }

    private static String getKey(String key) {
        return getConnect().get(key);
    }

    private static String setKey(String key, String value) {
        return getConnect().set(key, value);
    }
}

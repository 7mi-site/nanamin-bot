package xyz.n7mn.dev.api;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Money {

    private static final File config = new File("./config-redis.yml");
    private static long money = 1000L;

    @Nullable
    public static Long get(String memberId){
        YamlMapping ConfigYml = null;
        try {
            if (!config.exists()){
                config.createNewFile();

                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml = builder.add(
                        "RedisServer", "127.0.0.1"
                ).add(
                        "RedisPort", String.valueOf(Protocol.DEFAULT_PORT)
                ).add(
                        "RedisPass", ""
                ).build();

                try {
                    PrintWriter writer = new PrintWriter(config);
                    writer.print(ConfigYml.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                ConfigYml = Yaml.createYamlInput(config).readYamlMapping();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.gc();
            return null;
        }

        JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
        Jedis jedis = pool.getResource();
        jedis.auth(ConfigYml.string("RedisPass"));

        try {
            money = Long.parseLong(jedis.get("nanamibot:money:" + memberId));
        } catch (Exception e){
            e.printStackTrace();
        }

        jedis.close();
        pool.close();

        System.gc();

        return money;
    }

    public static void set(String memberId, Long money){
        System.gc();

        YamlMapping ConfigYml = null;
        try {
            if (!config.exists()){
                config.createNewFile();

                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml = builder.add(
                        "RedisServer", "127.0.0.1"
                ).add(
                        "RedisPort", String.valueOf(Protocol.DEFAULT_PORT)
                ).add(
                        "RedisPass", ""
                ).build();

                try {
                    PrintWriter writer = new PrintWriter(config);
                    writer.print(ConfigYml.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                ConfigYml = Yaml.createYamlInput(config).readYamlMapping();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.gc();
            return;
        }

        JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
        Jedis jedis = pool.getResource();
        jedis.auth(ConfigYml.string("RedisPass"));

        if (money != null){
            jedis.set("nanamibot:money:" + memberId, money.toString());
        } else {
            jedis.set("nanamibot:money:" + memberId, "100");
        }

        jedis.close();
        pool.close();

        System.gc();
    }

}

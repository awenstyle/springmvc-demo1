package com.awen.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.awen.mvc.dao.UserDaoImpl;

@ContextConfiguration(locations = "classpath*:applicationContext-redis.xml")
public class RedisTest {
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private UserDaoImpl userDaoImpl;

	@Test
	public void testSpringRedis() {
		// stringRedisTemplate的操作
		// String读写
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext-redis.xml" });
		JedisConnectionFactory connectionFactory = (JedisConnectionFactory) context.getBean("connectionFactory");
		RedisTemplate redisTemplate = (RedisTemplate) context.getBean("redisTemplate");
		UserDaoImpl userDao = (UserDaoImpl) context.getBean("userDaoImpl");
		redisTemplate.delete("myStr");
		redisTemplate.opsForValue().set("myStr", "skyLine");
		System.out.println(redisTemplate.opsForValue().get("myStr"));
		System.out.println("---------------");

		// List读写
		redisTemplate.delete("myList");
		redisTemplate.opsForList().rightPush("myList", "T");
		redisTemplate.opsForList().rightPush("myList", "L");
		redisTemplate.opsForList().leftPush("myList", "A");
		List<String> listCache = redisTemplate.opsForList().range("myList", 0, -1);
		for (String s : listCache) {
			System.out.println(s);
		}
		System.out.println("---------------");

		// Set读写
		redisTemplate.delete("mySet");
		redisTemplate.opsForSet().add("mySet", "A");
		redisTemplate.opsForSet().add("mySet", "B");
		redisTemplate.opsForSet().add("mySet", "C");
		Set<String> setCache = redisTemplate.opsForSet().members("mySet");
		for (String s : setCache) {
			System.out.println(s);
		}
		System.out.println("---------------");

		// Hash读写
		redisTemplate.delete("myHash");
		redisTemplate.opsForHash().put("myHash", "BJ", "北京");
		redisTemplate.opsForHash().put("myHash", "SH", "上海");
		redisTemplate.opsForHash().put("myHash", "HN", "河南");
		Map<String, String> hashCache = redisTemplate.opsForHash().entries("myHash");
		for (Map.Entry entry : hashCache.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue());
		}
		System.out.println("---------------");
	}
}

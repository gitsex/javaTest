package com.ecmoho.sycm.schq.processor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;


public class TestRedis {

	public static void main(String[] args) {
		ApplicationContext ac=new ClassPathXmlApplicationContext("conf/applicationContext.xml");
		StringRedisTemplate st= (StringRedisTemplate) ac.getBean("redisTemplate");	
//		for(int i=0;i<10;i++){
//			st.opsForList().rightPush("test1", "ÕÅÈý£º"+i);
//		}
	
	}
}

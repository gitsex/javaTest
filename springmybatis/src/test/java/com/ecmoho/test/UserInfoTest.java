package com.ecmoho.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ecmoho.test.service.UserInfoService;

import javax.annotation.Resource;

/**
 * Created by meidejing on 2016/6/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring.xml","classpath:spring/spring-mybatis.xml"})
public class UserInfoTest {
    @Resource(name = "userInfoServiceImpl")
    private UserInfoService userInfoService;
    @Test
    public void test1(){
        System.out.println(userInfoService.getUserInfo(1));
    }
}

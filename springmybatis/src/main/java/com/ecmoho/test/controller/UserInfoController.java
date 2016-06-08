package com.ecmoho.test.controller;

import com.ecmoho.test.model.UserInfo;
import com.ecmoho.test.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by meidejing on 2016/6/8.
 */
@Controller
@RequestMapping(value = "/UserInfo")
public class UserInfoController {
    @Resource(name = "userInfoServiceImpl")
    private UserInfoService userInfoService;

    @RequestMapping(value = "/getAllUserInfo",method = RequestMethod.GET)
    @ResponseBody
    public List<UserInfo> getAllUserInfo(){
        return userInfoService.getAllUserInfos();
    }
}

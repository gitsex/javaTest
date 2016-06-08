package com.ecmoho.test.service;

import com.ecmoho.test.model.UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by meidejing on 2016/6/8.
 */
public interface UserInfoService {
    public UserInfo getUserInfo(int id);
    public int addUserInfo(UserInfo userInfo);
    public int deleteUserInfo(int id);
    public int updateUserInfo(UserInfo userInfo);
    public List<UserInfo> getAllUserInfos();
}

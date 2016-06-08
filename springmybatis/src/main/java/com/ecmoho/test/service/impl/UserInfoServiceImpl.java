package com.ecmoho.test.service.impl;

import com.ecmoho.test.dao.UserInfoDao;
import com.ecmoho.test.model.UserInfo;
import com.ecmoho.test.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Created by meidejing on 2016/6/8.
 */
@Service("userInfoServiceImpl")
public class UserInfoServiceImpl implements UserInfoService{
    @Autowired
    private UserInfoDao userInfoDao;

    public UserInfo getUserInfo(int id) {
        return userInfoDao.getUserInfo(id);
    }

    public int addUserInfo(UserInfo userInfo) {
        return userInfoDao.addUserInfo(userInfo);
    }

    public int deleteUserInfo(int id) {
        return userInfoDao.deleteUserInfo(id);
    }

    public int updateUserInfo(UserInfo userInfo) {
        return userInfoDao.updateUserInfo(userInfo);
    }

    public List<UserInfo> getAllUserInfos() {
        return userInfoDao.getAllUserInfos();
    }


}

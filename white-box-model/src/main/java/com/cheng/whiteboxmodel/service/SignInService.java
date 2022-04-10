package com.cheng.whiteboxmodel.service;

import com.cheng.whiteboxmodel.entitys.UserInfo;
import com.cheng.whiteboxmodel.mapper.UserInfoMapper;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SignInService {
    @Resource
    private UserInfoMapper userInfoMapper;

    public List<UserInfo> verifyUser(String account) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        return userInfoMapper.selectByAccount(userInfo);
    }
}

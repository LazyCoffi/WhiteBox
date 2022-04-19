package com.cheng.whiteboxmodel.controller;


import com.cheng.whiteboxmodel.entitys.UserInfo;
import com.cheng.whiteboxmodel.service.UserInfoService;
import com.cheng.whiteboxmodel.utils.AuthToken;
import com.cheng.whiteboxmodel.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAccessController {
    UserInfoService userInfoService;

    @Autowired
    public void setSignInService(UserInfoService userInfoService) {this.userInfoService = userInfoService;}

    @RequestMapping(value = "/user-access/sign-in", method = RequestMethod.GET)
    public RestResponse valSignIn(String account, String password, String vCode) {
        List<UserInfo> userInfos = userInfoService.getUsers(account);

        if (userInfos.size() == 0) {
            return RestResponse.fail("未找到用户");
        }

        if (userInfos.size() > 1) {
            return RestResponse.fail("数据库异常");
        }

        // TODO: 验证码

        if (!password.equals(userInfos.get(0).getPassword())) {
            return RestResponse.fail("密码错误");
        }

        String token = userInfoService.storeToken(account);
        return RestResponse.authTokenPass(token);
    }

    @RequestMapping(value = "/user-access/sign-up", method = RequestMethod.POST)
    public RestResponse signUp(String account, String password, String vCode) {
        List<UserInfo> userInfos = userInfoService.getUsers(account);
        if (userInfos.size() > 0) {
            return RestResponse.fail("用户名已存在");
        }

        // TODO:验证码

        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(password);

        userInfoService.addNewUser(userInfo);

        return RestResponse.OK;
    }

    @RequestMapping(value = "/user-access/token-auth", method = RequestMethod.GET)
    @AuthToken
    public RestResponse tokenAccess() {
        return RestResponse.OK;
    }
}

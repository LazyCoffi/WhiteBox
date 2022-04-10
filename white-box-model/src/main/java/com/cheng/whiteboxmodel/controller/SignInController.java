package com.cheng.whiteboxmodel.controller;


import com.cheng.whiteboxmodel.entitys.UserInfo;
import com.cheng.whiteboxmodel.service.SignInService;
import com.cheng.whiteboxmodel.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SignInController {
    SignInService signInService;

    @Autowired
    public void setSignInService(SignInService signInService) {this.signInService = signInService}

    @RequestMapping(value = "/user-access/sign-in", method = RequestMethod.GET)
    public RestResponse valSignIn(String account, String password, String vCode) {
        List<UserInfo> userInfos = signInService.verifyUser(account);

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

        return RestResponse.OK;
    }
}

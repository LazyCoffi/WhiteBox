package com.cheng.whiteboxmodel.controller;


import com.cheng.whiteboxmodel.entitys.UserInfo;
import com.cheng.whiteboxmodel.service.UserInfoService;
import com.cheng.whiteboxmodel.utils.AuthToken;
import com.cheng.whiteboxmodel.utils.ConstantKit;
import com.cheng.whiteboxmodel.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;

@RestController
public class UserAccessController {
    UserInfoService userInfoService;

    @Autowired
    public void setSignInService(UserInfoService userInfoService) {this.userInfoService = userInfoService;}

    @RequestMapping(value = "/user-access/sign-in", method = RequestMethod.GET)
    public RestResponse signIn(String account, String password, String vCode) {
        return userInfoService.signInVerify(account, password, vCode);
    }

    @RequestMapping(value = "/user-access/danger-sign-in", method = RequestMethod.GET)
    public RestResponse dangerSignIn(String account, String password, String vCode) {
        return userInfoService.dangerSignInVerify(account, password, vCode);
    }

    @RequestMapping(value = "/user-access/sign-up", method = RequestMethod.POST)
    public RestResponse signUp(String account, String password, String vCode) {
        return userInfoService.signUpVerify(account, password, vCode);
    }

    @RequestMapping(value = "/user-access/token-auth", method = RequestMethod.GET)
    @AuthToken
    public RestResponse tokenAccess() {
        return RestResponse.OK;
    }

    @RequestMapping(value = "/user-access/send-signin-vcode", method = RequestMethod.POST)
    public RestResponse sendSignInVCode(String account) {
        return userInfoService.sendSignInVCode(account);
    }

    @RequestMapping(value = "/user-access/send-signup-vcode", method = RequestMethod.POST)
    public RestResponse sendSignUpVCode(String account, String email) {
        return userInfoService.sendSignUpVCode(account, email);
    }
}

package com.cheng.whiteboxmodel.service;

import com.cheng.whiteboxmodel.entitys.UserInfo;
import com.cheng.whiteboxmodel.mapper.UserInfoMapper;
import com.cheng.whiteboxmodel.utils.ConstantKit;
import com.cheng.whiteboxmodel.utils.Mail;
import com.cheng.whiteboxmodel.utils.RestResponse;
import com.cheng.whiteboxmodel.utils.TokenGenerator;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Random;

@Service
public class UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    public RestResponse signUpVerify(String account, String password, String vcode) {
        List<UserInfo> userInfoList = getUsers(account);
        if (!userInfoList.isEmpty()) {
            return RestResponse.fail("用户名已存在!");
        }

        Jedis jedis = new Jedis(ConstantKit.REDIS_IP);

        if (!jedis.exists("SignUp#" + account)) {
            return RestResponse.fail("未发送验证码或验证码已过期!");
        }

        if (!jedis.get("SignUp#" + account).equals(vcode)) {
            return RestResponse.fail("验证码错误!");
        }

        jedis.close();

        addNewUser(account, password);
        return RestResponse.OK;
    }

    public RestResponse signInVerify(String account, String password, String vcode) {
        List<UserInfo> userInfos = getUsers(account);

        if (userInfos.size() == 0) {
            return RestResponse.fail("未找到用户");
        }

        if (userInfos.size() > 1) {
            return RestResponse.fail("数据库异常");
        }

        Jedis jedis = new Jedis(ConstantKit.REDIS_IP);

        if (!jedis.exists("SignIn#" + account)) {
            return RestResponse.fail("未发送验证码或验证码已过期!");
        }

        if (!jedis.get("SignIn#" + account).equals(vcode)) {
            return RestResponse.fail("验证码错误!");
        }

        jedis.close();

        if (!password.equals(userInfos.get(0).getPassword())) {
            return RestResponse.fail("密码错误");
        }

        String token = storeToken(account);
        return RestResponse.authTokenPass(token);
    }

    public List<UserInfo> getUsers(String account) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        return userInfoMapper.selectByAccount(userInfo);
    }

    public RestResponse dangerSignInVerify(String account, String password, String vcode) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(password);
        List<UserInfo> userInfos = userInfoMapper.dangerSelect(userInfo);

        if (userInfos.size() == 0) {
            return RestResponse.fail("未找到用户");
        }

        Jedis jedis = new Jedis(ConstantKit.REDIS_IP);

        if (!jedis.exists("SignIn#" + account)) {
            return RestResponse.fail("未发送验证码或验证码已过期!");
        }

        if (!jedis.get("SignIn#" + account).equals(vcode)) {
            return RestResponse.fail("验证码错误!");
        }

        jedis.close();

        String token = storeToken(account);
        return RestResponse.authTokenPass(token);
    }

    public String storeToken(String account) {
        Jedis jedis = new Jedis(ConstantKit.REDIS_IP);

        String token = TokenGenerator.genToken(account);
        jedis.set(account, token);
        jedis.expire(account, ConstantKit.TOKEN_EXPIRE_TIME);

        jedis.set(token, account);
        jedis.expire(token, ConstantKit.TOKEN_EXPIRE_TIME);

        jedis.set(account + token, "" + System.currentTimeMillis());
        jedis.expire(TokenGenerator.getStampKey(account, token), ConstantKit.TOKEN_EXPIRE_TIME);

        jedis.close();

        return token;
    }

    public RestResponse sendSignInVCode(String account) {
        List<UserInfo> userInfoList = getUsers(account);
        if (userInfoList.isEmpty()) {
            return RestResponse.fail("用户名不存在!");
        }

        String email = userInfoList.get(0).getEmail();

        return sendVcode(account, email, "SignIn");
    }

    public RestResponse sendSignUpVCode(String account, String email) {
        return sendVcode(account, email, "SignUp");
    }

    private RestResponse sendVcode(String account, String email, String type) {
        Jedis jedis = new Jedis(ConstantKit.REDIS_IP);
        String vcode = "" + ((new Random()).nextInt(900000) + 100000);
        jedis.set(type + "#" + account, vcode);
        jedis.expire(type + "#" + account, ConstantKit.VCODE_EXPIRE_TIME);
        jedis.close();
        try {
            Mail.sendMail(vcode, email);
        } catch (MessagingException e) {
            e.printStackTrace();
            return RestResponse.fail("邮件发送失败!");
        }
        return RestResponse.OK;
    }

    public void addNewUser(String account, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(password);
        userInfoMapper.insert(userInfo);
    }
}

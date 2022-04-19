package com.cheng.whiteboxmodel.service;

import com.cheng.whiteboxmodel.entitys.UserInfo;
import com.cheng.whiteboxmodel.mapper.UserInfoMapper;
import com.cheng.whiteboxmodel.utils.ConstantKit;
import com.cheng.whiteboxmodel.utils.TokenGenerator;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    public List<UserInfo> getUsers(String account) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        return userInfoMapper.selectByAccount(userInfo);
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

    public void addNewUser(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }
}

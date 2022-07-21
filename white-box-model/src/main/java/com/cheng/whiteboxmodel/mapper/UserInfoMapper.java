package com.cheng.whiteboxmodel.mapper;

import com.cheng.whiteboxmodel.entitys.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Resource;
import java.util.List;

@Mapper
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<UserInfo> selectByAccount(UserInfo record);

    List<UserInfo> dangerSelect(UserInfo record);
}
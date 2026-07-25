package com.mall.user.service;

import cn.hutool.core.util.IdUtil;
import com.mall.common.dto.LoginDTO;
import com.mall.common.dto.LoginResultDTO;
import com.mall.common.entity.User;
import com.mall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public LoginResultDTO login(LoginDTO dto) {
        User user = userMapper.login(dto.getUsername(), dto.getPassword());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        LoginResultDTO result = new LoginResultDTO();
        result.setToken(IdUtil.fastSimpleUUID());

        LoginResultDTO.UserInfoDTO userInfo = new LoginResultDTO.UserInfoDTO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRole(user.getRole());
        result.setUser(userInfo);

        return result;
    }

    public void register(User user) {
        User existUser = userMapper.findByUsername(user.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        userMapper.insert(user);
    }
}
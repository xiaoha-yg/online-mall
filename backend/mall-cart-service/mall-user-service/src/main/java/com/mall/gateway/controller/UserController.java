package com.mall.gateway.controller;

import com.mall.common.dto.*;
import com.mall.common.entity.User;
import com.mall.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginResultDTO> login(@RequestBody LoginDTO dto) {
        return Result.success("登录成功", userService.login(dto));
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        userService.register(user);
        return Result.success("注册成功", null);
    }

    @GetMapping("/info")
    public Result<User> info(@RequestParam Long id) {
        return Result.success(null);
    }
}
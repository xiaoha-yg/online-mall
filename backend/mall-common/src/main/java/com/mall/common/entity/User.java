package com.mall.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
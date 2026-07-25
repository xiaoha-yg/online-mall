package com.mall.common.dto;

import lombok.Data;

@Data
public class LoginResultDTO {
    private String token;
    private UserInfoDTO user;

    @Data
    public static class UserInfoDTO {
        private Long id;
        private String username;
        private String role;
    }
}
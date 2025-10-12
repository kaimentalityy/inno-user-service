package com.innowise.orderservice.model.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
}

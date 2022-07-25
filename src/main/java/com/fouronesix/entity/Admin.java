package com.fouronesix.entity;

import lombok.Data;

@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private Integer adminType;
}

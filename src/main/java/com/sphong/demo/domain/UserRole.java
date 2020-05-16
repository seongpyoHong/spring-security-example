package com.sphong.demo.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private String roleName;
    UserRole(String roleName) {
        this.roleName = roleName;
    }
}
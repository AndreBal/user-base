package com.innowise.userserver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    //@JsonIgnore
    private int id;
    private String login;
    //@JsonIgnore
    private String password;
    private Role role;
    private String email;
    private String phone;
    @AllArgsConstructor
    @Getter
    public enum Role {
        ADMIN(1),
        VIEWER(2);

        private final int roleId;

        public static Role fromId(int roleId) {
            for (Role role : Role.values()) {
                if (role.getRoleId() == roleId) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Invalid role: " + roleId);
        }
    }
}

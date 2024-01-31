package com.sayas.filmhub.domain.user.dto;

import java.util.Set;

public class UserCredentialsDto {

    private final String email;
    private final String password;
    private final String username;
    private final Set<String> roles;

    private final boolean shadowBanned;
    public UserCredentialsDto(String email, String password, String username, Set<String> roles, boolean shadowBanned) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
        this.shadowBanned=shadowBanned;
    }

    public boolean isShadowBanned() {
        return shadowBanned;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
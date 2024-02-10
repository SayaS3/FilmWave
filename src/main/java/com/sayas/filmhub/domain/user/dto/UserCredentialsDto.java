package com.sayas.filmhub.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
public class UserCredentialsDto {

    private final String email;
    private final String password;
    private final String username;
    private final Set<String> roles;

    private final boolean shadowBanned;



}
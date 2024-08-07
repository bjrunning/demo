package com.example.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    private String firstName;

    private String lastName;

    private String role;

    private String email;

    private String password;
}

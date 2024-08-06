package com.example.dto;

import com.example.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private List<Role> roles;
}

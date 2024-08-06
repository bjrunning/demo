package com.example.service;

import com.example.dto.UserCreateDTO;
import com.example.dto.UserDTO;
import com.example.dto.UserUpdateDTO;
import com.example.model.User;

import java.util.List;

public interface UserService {

    void saveUser(UserCreateDTO userCreateDTO);

    void createUser(UserCreateDTO userCreateDTO, String roleName);

    User findUserByEmail(String email);

    List<UserDTO> getAll();

    User getUserById(Long id);

    void updateUser(Long id, UserUpdateDTO userUpdateDTO);

    void deleteUser(Long userId);

    void updateUserRole(Long userId, String roleName);
}

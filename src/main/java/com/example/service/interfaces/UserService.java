package com.example.service.interfaces;

import com.example.dto.user.UserCreateDTO;
import com.example.dto.user.UserDTO;
import com.example.dto.user.UserUpdateDTO;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    void saveUser(UserCreateDTO userCreateDTO);

    void createUser(UserCreateDTO userCreateDTO, String roleName);

    User findUserByEmail(String email);

    Page<UserDTO> getAll(Pageable pageable);

    Page<UserDTO> searchByUsername(String username, Pageable pageable);

    User getUserById(Long id);

    void updateUser(Long id, UserUpdateDTO userUpdateDTO);

    void deleteUser(Long userId);

    void updateUserRole(Long userId, String roleName);
}

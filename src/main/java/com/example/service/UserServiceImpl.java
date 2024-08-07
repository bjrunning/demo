package com.example.service;

import com.example.dto.user.UserCreateDTO;
import com.example.dto.user.UserDTO;
import com.example.dto.user.UserUpdateDTO;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.interfaces.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setUsername(userCreateDTO.getFirstName() + " " + userCreateDTO.getLastName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        Role role = roleRepository.findByName("USER");
        if (role == null){
            role = checkRoleExistUser();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void createUser(UserCreateDTO userCreateDTO, String roleName) {
        User user = new User();
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setUsername(userCreateDTO.getFirstName() + " " + userCreateDTO.getLastName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }

        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
            user.setLastName(userUpdateDTO.getLastName());
            user.setUsername(userUpdateDTO.getFirstName() + " " + userUpdateDTO.getLastName());
            userRepository.save(user);
        }
    }

    @PostConstruct
    public void initAdmin() {
        User adminUser = userRepository.findByEmail("admin@example.com");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setUsername("Admin Administrator");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Administrator");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("admin"));

            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
            }

            adminUser.setRoles(Arrays.asList(adminRole));
            userRepository.save(adminUser);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(role);
            user.setRoles(userRoles);
            userRepository.save(user);
        }
    }

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDTO(user))
                .collect(Collectors.toList());
    }

    private UserDTO mapToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        String[] str = user.getUsername().split(" ");
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(str[0]);
        userDTO.setLastName(str[1]);
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

    private Role checkRoleExistUser() {
        Role role = new Role();
        role.setName("USER");
        return roleRepository.save(role);
    }
}

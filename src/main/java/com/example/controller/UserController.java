package com.example.controller;

import com.example.dto.user.UserCreateDTO;
import com.example.dto.user.UserDTO;
import com.example.dto.user.UserUpdateDTO;
import com.example.model.User;
import com.example.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public String allUsers(Model model) {
        List<UserDTO> users = userService.getAll();
        model.addAttribute("users", users);
        return "users/users";
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userById(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "users/viewUser";
    }

    @GetMapping("/{userId}/editRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUserRoleForm(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "users/editUserRole";
    }

    @PostMapping("/{userId}/editRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUserRole(@PathVariable Long userId, @RequestParam String role) {
        userService.updateUserRole(userId, role);
        return "redirect:/users";
    }

    @GetMapping("/{userId}/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUserForm(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "users/editUser";
    }

    @PostMapping("/{userId}/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUser(@PathVariable Long userId,
                           @Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userId, userUpdateDTO);
        return "redirect:/users";
    }

    @GetMapping("/{userId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "redirect:/users";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addUser(Model model) {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        model.addAttribute("userCreateDTO", userCreateDTO);
        return "users/createUser";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createUser(@Valid @ModelAttribute("userCreateDTO") UserCreateDTO userCreateDTO,
                             BindingResult result, @RequestParam String role) {

        User existingUser = userService.findUserByEmail(userCreateDTO.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "Уже существует аккаунт, " +
                            "зарегистрированный с таким же адресом электронной почты");
        }

        if (result.hasErrors()) {
            return "users/createUser";
        }

        userService.createUser(userCreateDTO, role);
        return "redirect:/users";
    }
}

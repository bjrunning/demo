package com.example.controller;

import com.example.dto.UserCreateDTO;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "authentication/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        model.addAttribute("userCreateDTO", userCreateDTO);
        return "authentication/register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("userCreateDTO") UserCreateDTO userCreateDTO,
                               @RequestParam(value = "agree", required = false) String agree,
                               BindingResult result,
                               Model model) {

        User existingUser = userService.findUserByEmail(userCreateDTO.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "Уже существует аккаунт, " +
                            "зарегистрированный с таким же адресом электронной почты");
        }

        if (result.hasErrors()) {
            model.addAttribute("userCreateDTO", userCreateDTO);
            return "authentication/register";
        }

        if (!"on".equals(agree)) {
            model.addAttribute("error", "Вы должны согласиться с условиями.");
            return "authentication/register";
        }

        userService.saveUser(userCreateDTO);
        return "redirect:/login";
    }
}

package com.example.controller;

import com.example.dto.user.UserCreateDTO;
import com.example.model.User;
import com.example.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("authentication/login"));
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("authentication/register"));
    }

    @Test
    public void testRegistrationWithExistingEmail() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("John");
        userCreateDTO.setLastName("Doe");
        userCreateDTO.setEmail("existing@example.com");
        userCreateDTO.setPassword("password");

        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        when(userService.findUserByEmail("existing@example.com")).thenReturn(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/register/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", userCreateDTO.getFirstName())
                        .param("lastName", userCreateDTO.getLastName())
                        .param("email", userCreateDTO.getEmail())
                        .param("password", userCreateDTO.getPassword())
                        .param("agree", "on"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("authentication/register"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userCreateDTO", "email"));
    }

    @Test
    public void testRegistrationWithoutAgreement() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("John");
        userCreateDTO.setLastName("Doe");
        userCreateDTO.setEmail("new@example.com");
        userCreateDTO.setPassword("password");

        when(userService.findUserByEmail("new@example.com")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/register/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", userCreateDTO.getFirstName())
                        .param("lastName", userCreateDTO.getLastName())
                        .param("email", userCreateDTO.getEmail())
                        .param("password", userCreateDTO.getPassword())) // Не добавляем "agree"
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("authentication/register"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"));
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("John");
        userCreateDTO.setLastName("Doe");
        userCreateDTO.setEmail("new@example.com");
        userCreateDTO.setPassword("password");

        when(userService.findUserByEmail("new@example.com")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/register/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", userCreateDTO.getFirstName())
                        .param("lastName", userCreateDTO.getLastName())
                        .param("email", userCreateDTO.getEmail())
                        .param("password", userCreateDTO.getPassword())
                        .param("agree", "on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}

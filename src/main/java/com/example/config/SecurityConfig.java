package com.example.config;

import com.example.service.CustomUserDetailsService;
import jakarta.servlet.RequestDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/privacy_policy").permitAll()
                                .requestMatchers("/rules").permitAll()
                                .requestMatchers("/about").permitAll()
                                .requestMatchers("/topics").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/topics/create").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/topics/{id}/questions-answers").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/topics/{id}/edit").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/topics/{id}/delete").hasAuthority("ADMIN")
                                .requestMatchers("/topics/{id}/questions-answers/create").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/topics/{id}/questions-answers/{questionAnswerId}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/topics/{id}/questions-answers/{questionAnswerId}/edit").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/topics/{id}/questions-answers/{questionAnswerId}/delete").hasAuthority("ADMIN")
                                .requestMatchers("/users").hasAuthority("ADMIN")
                                .requestMatchers("/users/{userId}").hasAuthority("ADMIN")
                                .requestMatchers("/users/create").hasAuthority("ADMIN")
                                .requestMatchers("/users/{userId}/editRole").hasAuthority("ADMIN")
                                .requestMatchers("/users/{userId}/edit").hasAuthority("ADMIN")
                                .requestMatchers("/users/{userId}/delete").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                ).rememberMe(
                        rememberMe -> rememberMe
                                .tokenValiditySeconds(60 * 60 * 24 * 7)
                                .userDetailsService(userDetailsService)
                )
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(accessDeniedHandler()));
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            request.setAttribute("error", "Вы не имеете доступа к этой странице.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
            dispatcher.forward(request, response);
        };
    }
}

package com.kilometria.AccesoUsuarios.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/auth/**", "/login", "/auth/login", "/auth/registro",
                                 "/css/**", "/js/**", "/img/**").permitAll()

                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/vehiculos/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .permitAll()
            )

            .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, ex) ->
                        response.sendRedirect("/auth/login?denied"))
            )

            .sessionManagement(session -> session
                .invalidSessionUrl("/auth/login?expired")
                .maximumSessions(1)
                .expiredUrl("/auth/login?expired")
            )

            // *** MUY IMPORTANTE ***
            .httpBasic(basic -> basic.disable());   // desactiva el popup del navegador

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

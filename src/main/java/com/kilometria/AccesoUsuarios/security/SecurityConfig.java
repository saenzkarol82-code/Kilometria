package com.kilometria.AccesoUsuarios.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/index", "/login", "/registro", "/css/**", "/js/**", "/img/**","/cliente/vehiculos/catalogo","/vehiculos/**").permitAll()
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/vehiculos/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login") // Spring maneja el POST /login
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                
                .permitAll()
            )

            .logout(logout -> logout
              .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
              .logoutSuccessUrl("/index?logout")
              .permitAll()


            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

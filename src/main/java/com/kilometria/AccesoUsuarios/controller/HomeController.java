package com.kilometria.AccesoUsuarios.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String redirigirSegunRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
        return "admin-home";
     } else {
        return "cliente-home";
     }
    }

    

    
}

package com.kilometria.AccesoUsuarios.controller;
//User registration and login

import com.kilometria.AccesoUsuarios.model.*;
import com.kilometria.AccesoUsuarios.repository.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import com.kilometria.AccesoUsuarios.service.*;



@Controller
@RequestMapping("/auth")
public class AuthController {
     
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    
    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/")
    public String mostrarIndex() {
        return "index"; // nombre del template index.html en /templates
    }
    

    @GetMapping("/login")
    public String mostrarLogin() { //accion mostrar el login
        return "login"; // login.html
    }

    



    @GetMapping("/registro") //accion mostrar el formulario de registro
    public String mostrarRegistroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "/auth/registro"; // registro.html
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "form";
    }



    

   
    


    


    
}

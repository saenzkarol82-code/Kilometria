package com.kilometria.AccesoUsuarios.controller;
//User registration and login

import com.kilometria.AccesoUsuarios.model.*;
import com.kilometria.AccesoUsuarios.repository.*;
import com.kilometria.AccesoUsuarios.service.UsuarioService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;




@Controller
public class UsuarioController {

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;



    @GetMapping("/login")
      public String login() {
    return "login"; // login.html en templates este es del boton iniciar sesion 
    }
    
    @GetMapping("/home")  //home de usuarios y de admin 
    public String home() {
    return "home";
    }

    @GetMapping("/registro") //accion mostrar el formulario de registro
    public String mostrarRegistroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // registro.html
    }
     
   







   

   



    

   
    


    


    
}

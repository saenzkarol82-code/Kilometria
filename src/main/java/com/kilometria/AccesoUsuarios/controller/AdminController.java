//Publicación y gestión de vehículos

package com.kilometria.AccesoUsuarios.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;


import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.repository.VehiculoRepository;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    

    public AdminController(VehiculoRepository vehicleRepository, UsuarioRepository usuarioRepository) {
        this.vehiculoRepository = vehicleRepository;
        this.usuarioRepository = usuarioRepository;
       
    }

    @GetMapping("/usuarios")
     public String listarUsuarios(Model model) {
     model.addAttribute("usuarios", usuarioRepository.findAll());
     return "admin-usuarios";
    }

    @GetMapping("/vehiculos")
    public String verVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoRepository.findAll());
        return "admin-vehiculos"; // vista exclusiva admin
    }

    

}

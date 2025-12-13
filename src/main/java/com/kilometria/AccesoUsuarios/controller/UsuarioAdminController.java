package com.kilometria.AccesoUsuarios.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kilometria.AccesoUsuarios.model.RolUsuario;
import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.service.UsuarioService;


@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioAdminController {
    
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

     public UsuarioAdminController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }
    




    //formulario para usuario nuevo
    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", RolUsuario.values());
        return "usuario-form";
    }

    @PostMapping("/guardar")
     public String guardarUsuario(@ModelAttribute Usuario usuario) {

    if (usuario.getIdUsuario() != null) {
        // Modo edición
        Usuario existente = usuarioService.buscarPorId(usuario.getIdUsuario());
        usuario.setPassword(existente.getPassword());
    } else {
        // Modo creación: encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    }

     usuarioService.guardarSinEncriptar(usuario);
    return "redirect:/admin/usuarios";
    }


    // EDITAR usuarios
    @GetMapping("/editar/{id}")
public String editarUsuario(@PathVariable Long id, Model model) {
    Usuario usuario = usuarioService.buscarPorId(id);
    System.out.println("ID recibido: " + id);
    System.out.println("Usuario encontrado: " + usuario);

    model.addAttribute("usuario", usuario);
    model.addAttribute("roles", RolUsuario.values());
    return "usuario-form";
}


    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios";
    }
}

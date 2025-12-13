package com.kilometria.AccesoUsuarios.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kilometria.AccesoUsuarios.model.Estado;
import com.kilometria.AccesoUsuarios.model.Tipo;
import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.service.UsuarioService;
import com.kilometria.AccesoUsuarios.service.VehiculoService;


@Controller
@RequestMapping("/admin/vehiculos")
public class VehiculoAdminController {

    
    private final VehiculoService vehiculoService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;


    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        return "vehiculos";
    }

    public VehiculoAdminController(VehiculoService vehiculoService,
            UsuarioService usuarioService,
            UsuarioRepository usuarioRepository) {

        this.vehiculoService = vehiculoService;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/nuevo") 
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("tipos", Tipo.values());
        model.addAttribute("estados", Estado.values());
        model.addAttribute("usuarios", usuarioService.listarTodos()); // si usas propietarios
        return "vehiculo-form"; // formulario para publicar
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Vehiculo vehiculo) {
        vehiculoService.guardar(vehiculo);
        return "redirect:/admin/vehiculos";
    }

    @GetMapping("/editar/{id}")
      public String editar(@PathVariable Long id, Model model) {
      Vehiculo vehiculo = vehiculoService.buscarPorId(id);
     if (vehiculo == null) {
         return "redirect:/admin/vehiculos"; // o una página de error personalizada
    }

    model.addAttribute("vehiculo", vehiculo);
    model.addAttribute("tipos", Tipo.values());
    model.addAttribute("estados", Estado.values());
    model.addAttribute("usuarios", usuarioService.listarTodos());
    return "vehiculo-form";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return "redirect:/admin/vehiculos";
    }
    
}

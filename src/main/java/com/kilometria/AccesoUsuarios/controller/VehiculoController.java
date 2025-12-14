package com.kilometria.AccesoUsuarios.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.*;
import org.springframework.ui.Model;
//Publicación y gestión de vehículos


@Controller
public class VehiculoController {

    private final VehiculoRepository vehiculoRepository; //aqui llama a el repo
    private final UsuarioRepository usuarioRepository;

    public VehiculoController(VehiculoRepository vehiculoRepository, UsuarioRepository UsuarioRepository) {
        this.vehiculoRepository = vehiculoRepository;
         this.usuarioRepository = UsuarioRepository;
 //declara variable privada
    }


    @PostMapping("/nuevo")
    public String publicarVehiculo(@ModelAttribute Vehiculo vehiculo, Principal principal) {
         // aquí puedes asociar el vehículo al usuario autenticado
         String username = principal.getName();
         Usuario usuario = usuarioRepository.findByEmail(username); // asegúrate de tener este método
          vehiculo.setUsuario(usuario);
          vehiculo.setDisponible(true); // por defecto disponible al publicar
          vehiculoRepository.save(vehiculo);

          
          return "redirect:/admin/vehiculos";
    }

    @GetMapping("/lista")
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoRepository.findAll());
        return "vehiculos"; // vista con todos los vehículos
    }
}

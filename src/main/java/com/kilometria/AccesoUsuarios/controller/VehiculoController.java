package com.kilometria.AccesoUsuarios.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.VehiculoRepository;
import org.springframework.ui.Model;
//Publicación y gestión de vehículos


@Controller
public class VehiculoController {

    private final VehiculoRepository vehiculoRepository; //aqui llama a el repo

    public VehiculoController(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository; //declara variable privada
    }


    @PostMapping("/nuevo")
    public String publicarVehiculo(@ModelAttribute Vehiculo vehiculo, Principal principal) {
        // aquí puedes asociar el vehículo al usuario autenticado
        vehiculoRepository.save(vehiculo);
        return "redirect:/admin/vehiculos";
    }

    @GetMapping("/lista")
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoRepository.findAll());
        return "vehiculos"; // vista con todos los vehículos
    }
}

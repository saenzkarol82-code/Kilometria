package com.kilometria.AccesoUsuarios.controller;



import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.model.Tipo;
import com.kilometria.AccesoUsuarios.model.Estado;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.service.VehiculoService;

@Controller
@RequestMapping("/cliente/vehiculos")
public class ClienteVehiculoController {


    private final VehiculoService vehiculoService;
    private final UsuarioRepository usuarioRepository;

    public ClienteVehiculoController(VehiculoService vehiculoService, UsuarioRepository usuarioRepository) {
        this.vehiculoService = vehiculoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/nuevo") //formulario para publicar
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("tipos", Tipo.values());
        model.addAttribute("estados", Estado.values());
        return "cliente-vehiculo-form";
    }

    @PostMapping("/guardar") //Guardar vehículo publicado
    public String guardar(@ModelAttribute Vehiculo vehiculo, Principal principal) {

        // Obtener usuario autenticado
        var usuario = usuarioRepository.findByEmail(principal.getName());

        // Asociar vehículo al usuario
        vehiculo.setUsuario(usuario);

        vehiculoService.guardar(vehiculo);

        return "redirect:/cliente/vehiculos/mis-publicaciones";
    }

    @GetMapping("/mis-publicaciones")  //Mostrar solo los vehículos del cliente
    public String listarMisVehiculos(Model model, Principal principal) {

        var usuario = usuarioRepository.findByEmail(principal.getName());

        model.addAttribute("vehiculos", usuario.getVehiculos());

        return "cliente-vehiculos-lista";
    }

    @GetMapping("/editar/{id}")
public String editar(@PathVariable Long id, Model model, Principal principal) {

    var usuario = usuarioRepository.findByEmail(principal.getName());
    var vehiculo = vehiculoService.buscarPorId(id);

    // ✅ Seguridad: evitar que un cliente edite vehículos de otro usuario
    if (vehiculo == null || vehiculo.getUsuario().getIdUsuario() != usuario.getIdUsuario()) {
        return "redirect:/cliente/vehiculos/mis-publicaciones";
    }

    model.addAttribute("vehiculo", vehiculo);
    model.addAttribute("tipos", Tipo.values());
    model.addAttribute("estados", Estado.values());

    return "cliente-vehiculo-form"; // ✅ reutilizamos el mismo formulario
}

@GetMapping("/eliminar/{id}")
public String eliminar(@PathVariable Long id, Principal principal) {

    var usuario = usuarioRepository.findByEmail(principal.getName());
    var vehiculo = vehiculoService.buscarPorId(id);

    // ✅ Seguridad: solo puede eliminar sus propios vehículos
    if (vehiculo != null && vehiculo.getUsuario().getIdUsuario() == usuario.getIdUsuario()) {
        vehiculoService.eliminar(id);
    }

    return "redirect:/cliente/vehiculos/mis-publicaciones";
}


    
}



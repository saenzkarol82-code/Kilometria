package com.kilometria.AccesoUsuarios.controller;



import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/mis-publicaciones")  //Mostrar solo los vehículos del cliente
    public String listarMisVehiculos(Model model, Principal principal) {

        var usuario = usuarioRepository.findByEmail(principal.getName());

        model.addAttribute("vehiculos", usuario.getVehiculos());

        return "cliente-vehiculos-lista";
    }

    @PostMapping("/guardar") //Guardar vehículo publicado
    public String guardar(@ModelAttribute Vehiculo vehiculo,
                      @RequestParam(value = "imagen1", required = false) MultipartFile imagen1,
                      @RequestParam(value = "imagen2", required = false) MultipartFile imagen2,
                      Principal principal) {
    // Obtener usuario autenticado
    var usuario = usuarioRepository.findByEmail(principal.getName());
    vehiculo.setUsuario(usuario);

    // Procesar imágenes solo si existen
    if (imagen1 != null && !imagen1.isEmpty()) {
        String ruta1 = guardarArchivo(imagen1);
        vehiculo.setImagen1(ruta1);
    }
    if (imagen2 != null && !imagen2.isEmpty()) {
        String ruta2 = guardarArchivo(imagen2);
        vehiculo.setImagen2(ruta2);
    }

    vehiculoService.guardar(vehiculo);
    return "redirect:/cliente/vehiculos/mis-publicaciones";
    }


    private String guardarArchivo(MultipartFile archivo) {
    try {
        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        String ruta = "src/main/resources/static/img/vehiculos/" + nombreArchivo;
        archivo.transferTo(new File(ruta));
        return "/img/vehiculos/" + nombreArchivo;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
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

@GetMapping("/catalogo")
public String verCatalogo(Model model) {
    model.addAttribute("vehiculos", vehiculoService.listarDisponibles());
    return "catalogo";
}

    
}



package com.kilometria.AccesoUsuarios.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.*;
import com.kilometria.AccesoUsuarios.service.VehiculoService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;
//Publicación y gestión de vehículos


@Controller
public class VehiculoController {

     private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SpringTemplateEngine templateEngine;
    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoRepository vehiculoRepository,
                              UsuarioRepository usuarioRepository,
                              SpringTemplateEngine templateEngine,
                              VehiculoService vehiculoService) {
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.templateEngine = templateEngine;
        this.vehiculoService = vehiculoService; // ✅ ahora sí se inyecta
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

    @GetMapping("/reportes/vehiculo/{id}")
    public void generarReporteVehiculo(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Context context = new Context();
        context.setVariable("vehiculo", vehiculo);

        String html = templateEngine.process("reporte-vehiculo", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte-vehiculo.pdf");

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(response.getOutputStream());
    }

    @GetMapping("/vehiculos/{id}")
public String detalleVehiculo(@PathVariable Long id, Model model) {
    Vehiculo vehiculo = vehiculoService.buscarPorId(id);
    if (vehiculo == null) {
        return "redirect:/catalogo"; // o una página de error
    }

    model.addAttribute("vehiculo", vehiculo);
    return "detalle-vehiculo"; // nombre del nuevo template
}



    
}




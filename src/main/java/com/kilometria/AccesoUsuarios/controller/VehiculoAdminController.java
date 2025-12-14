package com.kilometria.AccesoUsuarios.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.kilometria.AccesoUsuarios.model.Estado;
import com.kilometria.AccesoUsuarios.model.Tipo;
import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.service.UsuarioService;
import com.kilometria.AccesoUsuarios.service.VehiculoService;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;


import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/admin/vehiculos")
public class VehiculoAdminController {

    
    private final VehiculoService vehiculoService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final SpringTemplateEngine templateEngine;


    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        return "vehiculos";
    }

    public VehiculoAdminController(VehiculoService vehiculoService,
            UsuarioService usuarioService,
            UsuarioRepository usuarioRepository, SpringTemplateEngine templateEngine) {

        this.vehiculoService = vehiculoService;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/nuevo") 
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("tipos", Tipo.values());
        model.addAttribute("estados", Estado.values());
        model.addAttribute("usuarios", usuarioService.listarTodos()); // si usas propietarios
        return "vehiculo-form"; // formulario para publicar
    }

    @PostMapping("/cliente/vehiculos/guardar")
     public String guardar(@ModelAttribute Vehiculo vehiculo,
                      @RequestParam("imagen1") MultipartFile imagen1,
                      @RequestParam("imagen2") MultipartFile imagen2) {
     if (!imagen1.isEmpty()) {
        vehiculo.setImagen1(imagen1.getOriginalFilename());
        // aquí deberías guardar físicamente el archivo en disco o nube
    }
    if (!imagen2.isEmpty()) {
        vehiculo.setImagen2(imagen2.getOriginalFilename());
    }

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

    @GetMapping("/reporte/{id}")
public void generarReporteVehiculo(@PathVariable Long id, HttpServletResponse response) {
    try {
        Vehiculo vehiculo = vehiculoService.buscarPorId(id);
        Context context = new Context();
        context.setVariable("vehiculo", vehiculo);

        String html = templateEngine.process("reporte-vehiculo", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte-vehiculo.pdf");

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(response.getOutputStream());

    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/buscar")
public String buscarVehiculos(@RequestParam String query, Model model) {
    List<Vehiculo> resultados = vehiculoService.buscarPorQuery(query);
    model.addAttribute("vehiculos", resultados);
    return "admin-vehiculos"; // usa el nombre de tu plantilla
}




    
}

package com.kilometria.AccesoUsuarios.controller;
//User registration and login

import com.kilometria.AccesoUsuarios.model.*;
import com.kilometria.AccesoUsuarios.repository.*;
import com.kilometria.AccesoUsuarios.service.UsuarioService;
import com.kilometria.AccesoUsuarios.service.VehiculoService;

import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;







@Controller
public class UsuarioController {

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, VehiculoService vehiculoService, SpringTemplateEngine templateEngine ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.vehiculoService = vehiculoService;
        this.templateEngine = templateEngine;
    }

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final VehiculoService vehiculoService;
    private final SpringTemplateEngine templateEngine;

    @GetMapping({"/", "/index"})
    public String index() {
        return "index"; // nombre del archivo index.html en templates
    }

    @GetMapping("/login")
      public String login() {
    return "login"; // login.html en templates este es del boton iniciar sesion 
    }

    @GetMapping("/registro")
    public String mostrarRegistroForm(Model model) {
    model.addAttribute("usuario", new Usuario()); // objeto vacío para el formulario
    return "registro"; // nombre del archivo registro.html en templates
    }

    
    @PostMapping("/registro/guardar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
    try {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        

        // Asignar rol automáticamente
        usuario.setRol(RolUsuario.COMPRADOR); // o VENDEDOR, según tu lógica

        usuarioRepository.save(usuario);
        model.addAttribute("exito", "Usuario registrado correctamente");
        return "registro";
    } catch (Exception e) {
        model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
        return "registro";
    }
}

@GetMapping("/reportes/usuario/{id}")
public void generarReporteUsuario(@PathVariable Long id, HttpServletResponse response) throws Exception {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Variables para Thymeleaf
    Context context = new Context();
    context.setVariable("usuario", usuario);

    // Renderizar HTML
    String html = templateEngine.process("reporte-usuario", context);

    // Exportar extensión PDF
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=reporte-usuario.pdf");

    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocumentFromString(html);
    renderer.layout();
    renderer.createPDF(response.getOutputStream());
}



}


   
     
   







   

   



    

   
    


    


    


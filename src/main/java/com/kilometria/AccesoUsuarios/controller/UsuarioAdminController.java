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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.kilometria.AccesoUsuarios.model.RolUsuario;
import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.service.UsuarioService;
import com.kilometria.AccesoUsuarios.service.VehiculoService;

import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioAdminController {
    
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final VehiculoService vehiculoService;
    private final SpringTemplateEngine templateEngine;
    

     public UsuarioAdminController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, VehiculoService vehiculoService, SpringTemplateEngine templateEngine, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.vehiculoService = vehiculoService;
        this.templateEngine = templateEngine;
        this.usuarioRepository = usuarioRepository;
        
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

    

    @GetMapping("/reporte/{id}")
public void generarReporteUsuario(@PathVariable Long id, HttpServletResponse response) {
    try {
        Usuario usuario = usuarioService.buscarPorId(id);
        Context context = new Context();
        context.setVariable("usuario", usuario);

        String html = templateEngine.process("reporte-usuario", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte-usuario.pdf");

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(response.getOutputStream());

    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/buscar")
public String buscarUsuarios(@RequestParam String query, Model model) {
    List<Usuario> resultados = usuarioService.buscarPorQuery(query);
    model.addAttribute("usuarios", resultados);
    return "admin-usuarios";
}





}

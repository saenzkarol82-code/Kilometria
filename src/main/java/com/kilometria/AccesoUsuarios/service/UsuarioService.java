

package com.kilometria.AccesoUsuarios.service;

import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registrar nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {
        // Validar si el email ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("El correo ya está registrado");
        }
        // Encriptar contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    //enlista usuarios 
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

   public Usuario guardarSinEncriptar(Usuario usuario) {
     return usuarioRepository.save(usuario);
   }


    // Validar credenciales de login
    public Usuario validarCredenciales(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario; // credenciales correctas
        }
        return null; // credenciales inválidas
    }

    // Buscar usuario por ID
    public Usuario buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElse(null);
    }


    public void eliminar(Long id) { //eliminar usuario 
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> buscarPorFiltros(Long id, String rol, String email) {
    List<Usuario> todos = usuarioRepository.findAll();

    return todos.stream()
        .filter(u -> id == null || u.getIdUsuario().equals(id))
        .filter(u -> rol == null || u.getRol().name().equalsIgnoreCase(rol))
        .filter(u -> email == null || u.getEmail().toLowerCase().contains(email.toLowerCase()))
        .collect(Collectors.toList());
}

   public List<Usuario> buscarPorQuery(String query) {
    List<Usuario> todos = usuarioRepository.findAll();

    return todos.stream()
        .filter(u -> {
            if (query.matches("\\d+")) {
                return u.getIdUsuario().equals(Long.parseLong(query));
            }
            if (u.getRol().name().equalsIgnoreCase(query)) {
                return true;
            }
            return u.getEmail().toLowerCase().contains(query.toLowerCase());
        })
        .collect(Collectors.toList());
}

}

   
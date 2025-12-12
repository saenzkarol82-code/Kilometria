

package com.kilometria.AccesoUsuarios.service;

import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
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
}

        // ALL Auto-generated method stub
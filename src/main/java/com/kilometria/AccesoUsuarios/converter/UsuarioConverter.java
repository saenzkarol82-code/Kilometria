package com.kilometria.AccesoUsuarios.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;

@Component
public class UsuarioConverter implements Converter<String, Usuario> {

    private final UsuarioRepository usuarioRepository;

    public UsuarioConverter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario convert(String id) {
        return usuarioRepository.findById(Long.valueOf(id)).orElse(null);
    }
}

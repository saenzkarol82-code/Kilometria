package com.kilometria.AccesoUsuarios.service;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.repository.VehiculoRepository;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository, UsuarioRepository usuarioRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo buscarPorId(Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    public void guardar(Vehiculo vehiculo) {
    Usuario propietario = usuarioRepository.findById(vehiculo.getUsuario().getIdUsuario())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    vehiculo.setUsuario(propietario);
    vehiculoRepository.save(vehiculo);
}


    public void eliminar(Long id) {
        vehiculoRepository.deleteById(id);
    }

    public List<Vehiculo> listarPublicados() {
        return vehiculoRepository.findByDisponibleTrue();
    }
    public List<Vehiculo> listarDisponibles() {
    return vehiculoRepository.findByDisponibleTrue();

    
    
}



    public List<Vehiculo> buscarPorQuery(String query) {
        List<Vehiculo> todos = vehiculoRepository.findAll();

        return todos.stream()
            .filter(v -> {
                // Buscar por ID si el query es numérico
                if (query.matches("\\d+")) {
                    return v.getIdVehiculo().equals(Long.parseLong(query));
                }
                // Buscar por marca
                if (v.getMarca() != null && v.getMarca().toLowerCase().contains(query.toLowerCase())) {
                    return true;
                }
                // Buscar por modelo
                if (v.getModelo() != null && v.getModelo().toLowerCase().contains(query.toLowerCase())) {
                    return true;
                }
                // Buscar por propietario (email del usuario asociado)
                if (v.getUsuario() != null && v.getUsuario().getEmail() != null &&
                    v.getUsuario().getEmail().toLowerCase().contains(query.toLowerCase())) {
                    return true;
                }
                return false;
            })
            .collect(Collectors.toList());
    }
}





   

    


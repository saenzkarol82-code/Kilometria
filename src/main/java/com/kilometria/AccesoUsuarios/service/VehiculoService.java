package com.kilometria.AccesoUsuarios.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.VehiculoRepository;


@Service
public class VehiculoService {

    private final VehiculoRepository repo;

    public VehiculoService(VehiculoRepository repo) {
        this.repo = repo;
    }

    public List<Vehiculo> listarTodos() {
        return repo.findAll();
    }

    public Vehiculo buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void guardar(Vehiculo vehiculo) {
        repo.save(vehiculo);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
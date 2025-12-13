package com.kilometria.AccesoUsuarios.repository;

import com.kilometria.AccesoUsuarios.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Ejemplo de query personalizada
   // List<Vehiculo> findByMarca(String marca);
  //  List<Vehiculo> findByDisponibleTrue();
}

package com.kilometria.AccesoUsuarios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kilometria.AccesoUsuarios.model.Estado;
import com.kilometria.AccesoUsuarios.model.RolUsuario;
import com.kilometria.AccesoUsuarios.model.Tipo;
import com.kilometria.AccesoUsuarios.model.Usuario;
import com.kilometria.AccesoUsuarios.model.Vehiculo;
import com.kilometria.AccesoUsuarios.repository.UsuarioRepository;
import com.kilometria.AccesoUsuarios.repository.VehiculoRepository;

@Configuration
public class DataInitializer {

    @Autowired
    private final VehiculoRepository vehiculoRepository;

    DataInitializer(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, VehiculoRepository vehiculoRepository) {
        return args -> {
            if (usuarioRepository.findByEmail("Karoladmin@gmail.com") == null) {
                Usuario admin = new Usuario();
                admin.setNombre("karol admin");
                admin.setApellido("saenz");
                admin.setEmail("Karoladmin@gmail.com");
                admin.setPassword(new BCryptPasswordEncoder().encode("123"));
                admin.setRol(RolUsuario.ADMIN);

                usuarioRepository.save(admin);
                System.out.println("✅ Usuario admin creado con éxito");
            }

            if (vehiculoRepository.findAll().isEmpty()) {
                Usuario admin = usuarioRepository.findByEmail("admin@system.com");

                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setMarca("Toyota");
                vehiculo.setModelo("Corolla");
                vehiculo.setPrecio(25000.00);
                vehiculo.setKilometraje(50000);
                vehiculo.setTipo(Tipo.SEDAN);
                vehiculo.setEstado(Estado.USADO);
                vehiculo.setDescripcion("Vehículo de prueba inicial");
                vehiculo.setDisponible(true);
                vehiculo.setUsuario(admin);

                vehiculoRepository.save(vehiculo);
                System.out.println("🚗 Vehículo de ejemplo creado con éxito");
                } else {
             
                  System.out.println("Vehicles️ already exist in the database");

            }
        };
    }
}

package cl.duocuc.masterbikes.usuarios_clientes_api.repository;

import cl.duocuc.masterbikes.usuarios_clientes_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByRut(String rut);
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByActivo(String activo);
    boolean existsByRut(String rut);
    boolean existsByCorreo(String correo);
}

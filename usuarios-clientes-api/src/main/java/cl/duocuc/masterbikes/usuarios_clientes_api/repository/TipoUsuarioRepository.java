package cl.duocuc.masterbikes.usuarios_clientes_api.repository;

import cl.duocuc.masterbikes.usuarios_clientes_api.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
}

package cl.duocuc.masterbikes.productos_inventario_api.repository;

import cl.duocuc.masterbikes.productos_inventario_api.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByCodigo(String codigo);
}

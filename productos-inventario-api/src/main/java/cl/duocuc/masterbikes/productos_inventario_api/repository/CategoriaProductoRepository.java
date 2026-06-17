package cl.duocuc.masterbikes.productos_inventario_api.repository;

import cl.duocuc.masterbikes.productos_inventario_api.model.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {

    boolean existsByNombre(String nombre);
}

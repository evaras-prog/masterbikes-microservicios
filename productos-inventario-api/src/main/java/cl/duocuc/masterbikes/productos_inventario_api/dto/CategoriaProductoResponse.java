package cl.duocuc.masterbikes.productos_inventario_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaProductoResponse {

    private Long idCategoria;
    private String nombre;
    private String descripcion;
}

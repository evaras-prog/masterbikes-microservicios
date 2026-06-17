package cl.duocuc.masterbikes.productos_inventario_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaProductoRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres")
    private String nombre;

    @Size(max = 200, message = "La descripcion no puedes superar los 200 caracteres")
    private String descripcion;
}

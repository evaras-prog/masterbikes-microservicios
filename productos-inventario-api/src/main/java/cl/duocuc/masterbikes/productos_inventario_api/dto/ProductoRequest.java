package cl.duocuc.masterbikes.productos_inventario_api.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;

@Getter
@Setter
public class ProductoRequest {

    @NotBlank(message = "El código de producto es obligatorio")
    @Size(max = 30, message = "El código no puede superar los 30 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 120, message ="El nombre no puede superar los 120 caracteres")
    private String nombre;

    @Size(max = 60, message = "La marca no puede superar los 60 caracteres")
    private String marca;

    @Size(max = 60, message = "El modelo no puede superar los 60 caracteres")
    private String modelo;

    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private Double precioVenta;

    @PositiveOrZero(message="El precio de arriendo por día no puede ser negativo")
    private Double precioArriendoDia;

    @PositiveOrZero(message = "El depósito de garantía no puede ser negativo")
    private Double depositoGarantia;

    @NotBlank(message = "Debe indicar si está disponible para venta")
    @Pattern(regexp = "S|N", message = "Disponible para venta solo puede ser S o N")
    private String disponibleVenta;

    @NotBlank(message = "Debe indicar si está disponible para arriendo")
    @Pattern(regexp = "S|N", message = "Disponible para arriendo solo puede ser S o N")
    private String disponibleArriendo;

    @Pattern(regexp = "S|N", message = "Activo solo puede ser S o N")
    private String activo;

    @NotNull(message = "La categoría del producto es obligatoria")
    private Long idCategoria;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

}

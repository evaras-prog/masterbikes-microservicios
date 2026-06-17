package cl.duocuc.masterbikes.productos_inventario_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoResponse {

    private Long idProducto;
    private String codigo;
    private String nombre;
    private String marca;
    private String modelo;
    private Double precioVenta;
    private Double precioArriendoDia;
    private Double depositoGarantia;
    private String disponibleVenta;
    private String disponibleArriendo;
    private String activo;
    private Integer stock;

    private Long idCategoria;
    private String nombreCategoria;
}

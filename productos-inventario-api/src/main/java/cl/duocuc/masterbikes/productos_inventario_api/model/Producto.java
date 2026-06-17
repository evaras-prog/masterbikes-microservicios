package cl.duocuc.masterbikes.productos_inventario_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "marca", length = 60)
    private String marca;

    @Column(name = "modelo", length = 60)
    private String modelo;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;

    @Column(name = "precio_arriendo_dia")
    private Double precioArriendoDia;

    @Column(name = "deposito_garantia")
    private Double depositoGarantia;

    @Column(name = "disponible_venta", nullable = false, length = 1)
    private String disponibleVenta;

    @Column(name = "disponible_arriendo", nullable = false, length = 1)
    private String disponibleArriendo;

    @Column(name = "activo", nullable = false, length = 1)
    private String activo;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaProducto categoriaProducto;

}

package cl.duocuc.masterbikes.productos_inventario_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria_producto")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nombre", nullable = false, unique = true, length = 60)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "categoriaProducto")
    private List<Producto> productos = new ArrayList<>();
}

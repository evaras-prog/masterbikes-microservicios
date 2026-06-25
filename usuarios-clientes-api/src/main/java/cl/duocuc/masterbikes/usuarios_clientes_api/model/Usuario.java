package cl.duocuc.masterbikes.usuarios_clientes_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
    @Schema(description = "Id único de usuario", example = "01")
    private Long idUsuario;

    @Column(name = "rut", nullable = false, unique = true, length = 9)
    private String rut;

    @Column(name = "nombres", nullable = false, length = 100)
    @Schema(description = "Nombre del usuario", example = "Juan Pablo")
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name ="correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name ="password", nullable = false, length = 200)
    private String password;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "activo", nullable = false, length = 1)
    private String activo;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "id_sucursal")
    private Long idSucursal;
}

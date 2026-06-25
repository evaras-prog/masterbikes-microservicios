package cl.duocuc.masterbikes.usuarios_clientes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponse {

    private Long idUsuario;
    private String rut;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;
    private String activo;

    private Long idTipoUsuario;
    private String nombreTipoUsuario;

    private Long idSucursal;
}

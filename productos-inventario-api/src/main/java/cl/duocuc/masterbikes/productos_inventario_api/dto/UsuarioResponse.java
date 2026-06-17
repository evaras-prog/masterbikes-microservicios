package cl.duocuc.masterbikes.productos_inventario_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {

    private Long idUsuario;
    private String rut;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String activo;

    private Long idTipoUsuario;
    private String nombreTipoUsuario;

    private Long idSucursal;
}

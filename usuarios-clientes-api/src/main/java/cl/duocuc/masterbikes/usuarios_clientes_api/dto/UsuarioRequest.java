package cl.duocuc.masterbikes.usuarios_clientes_api.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min=8, max = 9, message = "El RUT debe tener entre 8 y 9 caracteres")
    private String rut;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
    private String apellidos;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 200, message = "La contraseña debe tener entre 6 y 200 caracteres")
    private String password;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;

    @Pattern(regexp = "S|N", message = "El campo activo solo permite S o N")
    private String activo;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private Long idTipoUsuario;

    private Long idSucursal;
}

package cl.duocuc.masterbikes.usuarios_clientes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    @Schema(
            description = "Token JWT generado después de una autenticación exitosa",
            example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    private String token;

    @Schema(
            description = "Correo electrónico del usuario autenticado",
            example = "admin@masterbikes.cl"
    )
    private String correo;

    @Schema(
            description = "Nombre completo del usuario",
            example = "Administrador MasterBikes"
    )
    private String nombreCompleto;

    @Schema(
            description = "Rol o tipo de usuario",
            example = "ADMIN"
    )
    private String tipoUsuario;
}

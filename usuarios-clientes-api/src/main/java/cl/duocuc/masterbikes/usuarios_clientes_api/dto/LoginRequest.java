package cl.duocuc.masterbikes.usuarios_clientes_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener formato válido")
    @Schema(
            description = "Correo electrónico del usuario",
            example = "admin@masterbikes.cl"
    )
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(
            description = "Contraseña del usuario",
            example = "Admin1234"
    )
    private String password;
}
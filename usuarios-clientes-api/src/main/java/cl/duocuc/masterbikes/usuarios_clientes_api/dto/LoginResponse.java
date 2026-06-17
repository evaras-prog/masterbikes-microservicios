package cl.duocuc.masterbikes.usuarios_clientes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String correo;
    private String nombreCompleto;
    private String tipoUsuario;
}

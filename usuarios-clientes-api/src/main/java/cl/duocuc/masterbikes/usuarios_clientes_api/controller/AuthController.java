package cl.duocuc.masterbikes.usuarios_clientes_api.controller;

import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ApiResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.LoginRequest;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.LoginResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticación", description = "Operaciones de autenticación y generación de token JWT")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario con sus credenciales y retorna un token JWT para usar en los demás endpoints"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso, token generado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales de acceso", required = true)
            @Valid @RequestBody LoginRequest request
    ){

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Login exitoso",
                false,
                response
        ));
    }
}

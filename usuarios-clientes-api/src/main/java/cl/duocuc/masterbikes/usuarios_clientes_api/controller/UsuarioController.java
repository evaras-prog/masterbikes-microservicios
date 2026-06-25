package cl.duocuc.masterbikes.usuarios_clientes_api.controller;

import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ApiResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioRequest;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ProductoResponse;

import java.util.List;

@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios",
            description = "Obtiene el listado completo de usuarios registrados en el sistema"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> listarUsuarios(){

        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuarios encontrados correctamente",
                false,
                usuarios
        ));
    }


    @Operation(
            summary = "Buscar usuario por ID",
            description = "Obtiene un usuario específico usando su ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long idUsuario
    ){
        UsuarioResponse usuario = usuarioService.obtenerUsuarioPorId(idUsuario);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario encontrado correctamente",
                false,
                usuario
        ));
    }


    @Operation(
            summary = "Registrar un usuario",
            description = "Registrar un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El usuario ya existe")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario que se registra", required = true)
            @Valid @RequestBody UsuarioRequest request
    ){
        UsuarioResponse usuarioRegistrado = usuarioService.registrarUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Usuario registrado correctamente",
                        false,
                        usuarioRegistrado
                ));
    }



    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente por su ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1", required = true)
            @PathVariable Long idUsuario,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del usuario", required = true)
            @Valid @RequestBody UsuarioRequest request
    ){
        UsuarioResponse usuarioActualizado = usuarioService.actualizarUsuario(idUsuario, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario actualizado correctamente",
                false,
                usuarioActualizado
        ));
    }



    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema por su ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1", required = true)
            @PathVariable Long idUsuario
    ) {
        usuarioService.eliminarUsuario(idUsuario);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario eliminado correctamente",
                false,
                null
        ));
    }


    @Operation(
            summary = "Obtener producto desde microservicio",
            description = "Consulta un producto en productos-inventario-api usando comunicación entre microservicios (Feign)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "Error de comunicación con el microservicio de productos")
    })
    @GetMapping("/productos/{idProducto}")
    public ResponseEntity<ApiResponse<ProductoResponse>> obtenerProductoDesdeMicroservicio(
            @Parameter(description = "ID del producto a consultar", example = "1", required = true)
            @PathVariable Long idProducto
    ){
        ProductoResponse producto = usuarioService.obtenerProductoDesdeMicroservicio(idProducto);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Producto obtenido correctamente desde productos-inventario-api",
                false,
                producto
        ));
    }


    @GetMapping("/correo/{correo}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerPorCorreo(@PathVariable String correo){

        UsuarioResponse usuario = usuarioService.obtenerPorCorreo(correo);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario encontrado correctamente",
                false,
                usuario
        ));
    }
}

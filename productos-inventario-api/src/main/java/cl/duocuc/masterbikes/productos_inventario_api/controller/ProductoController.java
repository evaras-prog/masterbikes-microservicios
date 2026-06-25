package cl.duocuc.masterbikes.productos_inventario_api.controller;

import cl.duocuc.masterbikes.productos_inventario_api.dto.ApiResponse;
import cl.duocuc.masterbikes.productos_inventario_api.dto.ProductoRequest;
import cl.duocuc.masterbikes.productos_inventario_api.dto.ProductoResponse;
import cl.duocuc.masterbikes.productos_inventario_api.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duocuc.masterbikes.productos_inventario_api.dto.UsuarioResponse;

import java.util.List;

@Tag(name = "Productos", description = "Operaciones relacionadas con los productos del inventario")
@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Listar productos", description = "Obtiene el listado completo de productos del inventario")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> listarProductos(){

        List<ProductoResponse> productos = productoService.listarProductos();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Productos encontrados correctamente",
                false,
                productos
        ));
    }



    @Operation(summary = "Buscar producto por ID", description = "Obtiene un producto específico usando su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{idProducto}")
    public ResponseEntity<ApiResponse<ProductoResponse>> obtenerProductoPorId(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long idProducto
    ){

        ProductoResponse producto = productoService.obtenerProductoPorId(idProducto);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Producto encontrado correctamente",
                false,
                producto
        ));
    }



    @Operation(summary = "Registrar producto", description = "Registra un nuevo producto en el inventario")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Producto registrado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflicto con reglas de negocio")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponse>> registrarProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del producto a registrar", required = true)
            @Valid @RequestBody ProductoRequest request
    ){

        ProductoResponse productoRegistrado = productoService.registrarProducto(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Producto registrado correctamente",
                        false,
                        productoRegistrado
                ));
    }



    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflicto con reglas de negocio")
    })
    @PutMapping("/{idProducto}")
    public ResponseEntity<ApiResponse<ProductoResponse>> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", example = "1", required = true)
            @PathVariable Long idProducto,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del producto", required = true)
            @Valid @RequestBody ProductoRequest request
    ){

        ProductoResponse productoActualizado = productoService.actualizarProducto(idProducto, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Producto actualizado correctamente",
                false,
                productoActualizado
        ));
    }



    @Operation(summary = "Eliminar producto", description = "Elimina un producto del inventario por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", example = "1", required = true)
            @PathVariable Long idProducto
    ){

        productoService.eliminarProducto(idProducto);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Producto eliminado correctamente",
                false,
                null
        ));
    }



    @Operation(
            summary = "Obtener usuario desde microservicio",
            description = "Consulta un usuario en usuarios-clientes-api usando comunicación entre microservicios (Feign)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "Error de comunicación con el microservicio de usuarios")
    })
    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerUsuarioDesdeMicroservicio(
            @Parameter(description = "ID del usuario a consultar", example = "1", required = true)
            @PathVariable Long idUsuario
    ){

        UsuarioResponse usuario = productoService.obtenerUsuarioDesdeMicroservicio(idUsuario);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario obtenido correctamente desde usuarios-clientes-api",
                false,
                usuario
        ));
    }

    @Operation(summary = "Listar productos activos", description = "Obtiene el listado de productos con estado activo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> listarProductosActivos() {

        List<ProductoResponse> productos = productoService.listarProductosActivos();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Productos activos encontrados correctamente",
                false,
                productos
        ));
    }
}

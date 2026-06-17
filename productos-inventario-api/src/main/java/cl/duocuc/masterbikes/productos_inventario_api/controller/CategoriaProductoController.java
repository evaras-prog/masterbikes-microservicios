package cl.duocuc.masterbikes.productos_inventario_api.controller;

import cl.duocuc.masterbikes.productos_inventario_api.dto.ApiResponse;
import cl.duocuc.masterbikes.productos_inventario_api.dto.CategoriaProductoRequest;
import cl.duocuc.masterbikes.productos_inventario_api.dto.CategoriaProductoResponse;
import cl.duocuc.masterbikes.productos_inventario_api.service.CategoriaProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorías de Producto", description = "Operaciones relacionadas con las categorías de productos")
@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaProductoController {

    private final CategoriaProductoService categoriaProductoService;


    @Operation(summary = "Listar categorías", description = "Obtiene el listado completo de categorías de productos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaProductoResponse>>> listarCategorias(){

        List<CategoriaProductoResponse> categorias = categoriaProductoService.listarCategorias();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Categorías encontradas correctamente",
                false,
                categorias
        ));
    }


    @Operation(summary = "Buscar categoría por ID", description = "Obtiene una categoría de producto usando su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{idCategoria}")
    public ResponseEntity<ApiResponse<CategoriaProductoResponse>> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría", example = "1", required = true)
            @PathVariable Long idCategoria
    ){

        CategoriaProductoResponse categoria = categoriaProductoService.obtenerCategoriaPorId(idCategoria);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Categoría encontrada correctamente",
                false,
                categoria
        ));
    }


    @Operation(summary = "Registrar categoría", description = "Registra una nueva categoría de producto en el sistema")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Categoría registrada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "La categoría ya existe")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaProductoResponse>> registrarCategoria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la categoría a registrar", required = true)
            @Valid @RequestBody CategoriaProductoRequest request
    ){

        CategoriaProductoResponse categoriaRegistrada = categoriaProductoService.registrarCategoria(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Categoria registrada correctamente",
                        false,
                        categoriaRegistrada
                ));
    }


    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría existente por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{idCategoria}")
    public ResponseEntity<ApiResponse<CategoriaProductoResponse>> actualizarCategoria(
            @Parameter(description = "ID de la categoría a actualizar", example = "1", required = true)
            @PathVariable Long idCategoria,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la categoría", required = true)
            @Valid @RequestBody CategoriaProductoRequest request
    ){

        CategoriaProductoResponse categoriaActualizada = categoriaProductoService.actualizarCategoria(idCategoria, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Categoría actualizada correctamente",
                false,
                categoriaActualizada
        ));
    }


    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría de producto del sistema por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoría a eliminar", example = "1", required = true)
            @PathVariable Long idCategoria
    ){

        categoriaProductoService.eliminarCategoria(idCategoria);

        return ResponseEntity.noContent().build();
    }
}

package cl.duocuc.masterbikes.usuarios_clientes_api.client;


import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ApiResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ProductoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "productos-inventario-api",
        url = "${productos-inventario-api.url}"
)
public interface ProductoClient {

    @GetMapping("/api/v1/productos/{idProducto}")
    ApiResponse<ProductoResponse> obtenerProductoPorId(@PathVariable Long idProducto);
}

package cl.duocuc.masterbikes.productos_inventario_api.client;

import cl.duocuc.masterbikes.productos_inventario_api.dto.ApiResponse;
import cl.duocuc.masterbikes.productos_inventario_api.dto.UsuarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "usuarios-clientes-api",
        url = "${usuarios-clientes-api.url}"
)
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios/{idUsuario}")
    ApiResponse<UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long idUsuario);
}

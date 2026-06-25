package cl.duoc.api_gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Gateway", description = "Verificación del estado del API Gateway")
@RestController
@RequestMapping("/api/v1/gateway")
public class TestController {

    @Operation(summary = "Estado del Gateway", description = "Retorna el estado actual del API Gateway")
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok(Map.of(
                "servicio", "api-gateway",
                "estado", "activo",
                "rutas", "usuarios, auth, productos, categorias"
        ));
    }
}

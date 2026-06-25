package cl.duoc.api_gateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
public class GatewayController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${microservicios.usuarios-url}")
    private String usuariosUrl;

    @Value("${microservicios.productos-url}")
    private String productosUrl;

    // ── Rutas hacia usuarios-clientes-api (puerto 8081) ──────────────────────

    @RequestMapping({"/api/v1/usuarios", "/api/v1/usuarios/**"})
    public ResponseEntity<Object> proxyUsuarios(
            HttpServletRequest request,
            @RequestBody(required = false) Object body) {
        return proxy(request, body, usuariosUrl);
    }

    @RequestMapping({"/api/v1/auth", "/api/v1/auth/**"})
    public ResponseEntity<Object> proxyAuth(
            HttpServletRequest request,
            @RequestBody(required = false) Object body) {
        return proxy(request, body, usuariosUrl);
    }

    // ── Rutas hacia productos-inventario-api (puerto 8082) ───────────────────

    @RequestMapping({"/api/v1/productos", "/api/v1/productos/**"})
    public ResponseEntity<Object> proxyProductos(
            HttpServletRequest request,
            @RequestBody(required = false) Object body) {
        return proxy(request, body, productosUrl);
    }

    @RequestMapping({"/api/v1/categorias", "/api/v1/categorias/**"})
    public ResponseEntity<Object> proxyCategorias(
            HttpServletRequest request,
            @RequestBody(required = false) Object body) {
        return proxy(request, body, productosUrl);
    }

    // ── Método genérico de reenvío ────────────────────────────────────────────

    private ResponseEntity<Object> proxy(HttpServletRequest request, Object body, String baseUrl) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String targetUrl = baseUrl + path + (query != null ? "?" + query : "");

        // Reenviar headers originales (Authorization, Content-Type, etc.)
        // Se excluyen Content-Length y Transfer-Encoding para evitar conflictos
        // al re-serializar el body en el reenvío
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames())
                .stream()
                .filter(name -> !name.equalsIgnoreCase("content-length")
                             && !name.equalsIgnoreCase("transfer-encoding"))
                .forEach(name -> headers.add(name, request.getHeader(name)));

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(
                    targetUrl,
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    Object.class
            );
        } catch (HttpStatusCodeException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        }
    }
}

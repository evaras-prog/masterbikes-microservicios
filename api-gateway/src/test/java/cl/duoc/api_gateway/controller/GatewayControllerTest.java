package cl.duoc.api_gateway.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayControllerTest {

    private final TestController testController = new TestController();

    @Test
    @DisplayName("status: retorna 200 y estado activo")
    void status_retornaEstadoActivo() {
        // WHEN
        ResponseEntity<Map<String, String>> respuesta = testController.status();

        // THEN
        assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
        assertThat(respuesta.getBody()).containsEntry("estado", "activo");
        assertThat(respuesta.getBody()).containsEntry("servicio", "api-gateway");
    }
}
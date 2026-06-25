package cl.duocuc.masterbikes.usuarios_clientes_api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled("Requiere conexión a Oracle — se ejecuta solo con la Wallet disponible")
@SpringBootTest
@ActiveProfiles("test")
class UsuariosClientesApiApplicationTests {

	@Test
	void contextLoads() {
	}

}





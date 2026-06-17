package cl.duocuc.masterbikes.usuarios_clientes_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsuariosClientesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosClientesApiApplication.class, args);
	}

}

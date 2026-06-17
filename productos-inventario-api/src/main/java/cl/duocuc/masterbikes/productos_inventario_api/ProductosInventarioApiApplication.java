package cl.duocuc.masterbikes.productos_inventario_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductosInventarioApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosInventarioApiApplication.class, args);
	}

}

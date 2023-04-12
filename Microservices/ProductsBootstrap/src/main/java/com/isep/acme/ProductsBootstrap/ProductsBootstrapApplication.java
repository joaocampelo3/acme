package com.isep.acme.ProductsBootstrap;

import com.isep.acme.ProductsBootstrap.services.RPCService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductsBootstrapApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ProductsBootstrapApplication.class, args);

		// Create service thread
		RPCService server = new RPCService();
		server.start();

		// Finalize
		server.join();
	}

}

package com.sastelvio.inventory_service;

import com.sastelvio.inventory_service.model.Inventory;
import com.sastelvio.inventory_service.repository.InventoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	@Value("${server.port}")
	private int serverPort;

	@Autowired
	private EurekaInstanceConfigBean eurekaInstanceConfigBean;

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@PostConstruct
	public void init() {
		eurekaInstanceConfigBean.setNonSecurePort(serverPort);
	}

	//CARREGAR DADOS AO INICIAR A APLICACAO
	@Bean
	public CommandLineRunner loadData(InventoryRepository repository){
		return args -> {
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone_15");
			inventory1.setQuantity(100);

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("iphone_15_pro");
			inventory2.setQuantity(0);

			repository.save(inventory1);
			repository.save(inventory2);

		};
	}
}

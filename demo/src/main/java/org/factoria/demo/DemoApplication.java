package org.factoria.demo;

import org.factoria.demo.myFavoriteImage.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public IdWorker idWorker() {
		return new IdWorker(1,1);
	}

}

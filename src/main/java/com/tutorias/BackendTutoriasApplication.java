package com.tutorias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BackendTutoriasApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTutoriasApplication.class, args);
	}

}

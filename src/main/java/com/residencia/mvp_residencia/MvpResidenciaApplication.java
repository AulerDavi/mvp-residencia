package com.residencia.mvp_residencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MvpResidenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvpResidenciaApplication.class, args);
	}

}

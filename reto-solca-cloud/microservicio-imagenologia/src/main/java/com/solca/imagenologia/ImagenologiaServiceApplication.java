package com.solca.imagenologia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ImagenologiaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImagenologiaServiceApplication.class, args);
        System.out.println("✅ Microservicio Imagenología iniciado en puerto 8084");
    }
}
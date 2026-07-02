package com.solca.repositorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RepositorioServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepositorioServiceApplication.class, args);
        System.out.println("✅ Microservicio Repositorio iniciado en puerto 8085");
    }
}
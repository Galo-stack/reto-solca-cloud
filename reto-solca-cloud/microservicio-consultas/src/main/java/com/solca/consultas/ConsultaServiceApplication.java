package com.solca.consultas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ConsultaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsultaServiceApplication.class, args);
        System.out.println("✅ Microservicio Consultas iniciado en puerto 8082");
    }
}
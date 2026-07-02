package com.solca.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PacienteServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PacienteServiceApplication.class, args);
        System.out.println("✅ Microservicio Pacientes iniciado en puerto 8081");
    }
}
package com.solca.repositorio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    private static final int CONNECT_TIMEOUT_SEC = 5;
    private static final int READ_TIMEOUT_SEC = 10;
    private static final int MAX_MEMORY_SIZE = 2 * 1024 * 1024;

    @Value("${services.pacientes.url}")
    private String pacientesUrl;

    @Value("${services.consultas.url}")
    private String consultasUrl;

    @Value("${services.laboratorio.url}")
    private String laboratorioUrl;

    @Value("${services.imagenologia.url}")
    private String imagenologiaUrl;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create(
                ConnectionProvider.builder("solca-pool")
                        .maxConnections(50)
                        .pendingAcquireTimeout(Duration.ofSeconds(10))
                        .build())
                .responseTimeout(Duration.ofSeconds(READ_TIMEOUT_SEC));
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }

    @Bean
    public WebClient pacientesWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(pacientesUrl + "/api").build();
    }

    @Bean
    public WebClient consultasWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(consultasUrl + "/api").build();
    }

    @Bean
    public WebClient laboratorioWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(laboratorioUrl + "/api").build();
    }

    @Bean
    public WebClient imagenologiaWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(imagenologiaUrl + "/api").build();
    }
}
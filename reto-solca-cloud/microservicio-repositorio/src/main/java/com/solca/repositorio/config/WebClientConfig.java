package com.solca.repositorio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import java.time.Duration;

@Configuration
public class WebClientConfig {

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

    @Value("${services.auditoria.url}")
    private String auditoriaUrl;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create(
                ConnectionProvider.builder("solca-pool")
                        .maxConnections(50)
                        .pendingAcquireTimeout(Duration.ofSeconds(10))
                        .build())
                .responseTimeout(Duration.ofSeconds(READ_TIMEOUT_SEC))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
    }

    private ExchangeFilterFunction jwtForwardingFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                String authHeader = attrs.getRequest().getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    ClientRequest filtered = ClientRequest.from(request)
                            .header("Authorization", authHeader)
                            .build();
                    return Mono.just(filtered);
                }
            }
            return Mono.just(request);
        });
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder()
                .filter(jwtForwardingFilter())
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

    @Bean
    public WebClient auditoriaWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(auditoriaUrl + "/api").build();
    }
}

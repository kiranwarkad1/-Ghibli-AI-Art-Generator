package com.akhil.ghbli_sb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class AppConfig {

    @Value("${stability.api.base-url}")
    private String stabilityApiBaseUrl;

    @Bean
    public WebClient stabilityWebClient() {
        HttpClient httpClient = HttpClient.create();

        return WebClient.builder()
                .baseUrl(stabilityApiBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10 MB
                )
                .build();
    }
}

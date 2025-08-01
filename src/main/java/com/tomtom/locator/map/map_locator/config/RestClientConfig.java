package com.tomtom.locator.map.map_locator.config;

import com.tomtom.locator.map.map_locator.exception.ExternalServiceClientException;
import com.tomtom.locator.map.map_locator.exception.ExternalServiceServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Configuration
@Slf4j
class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .defaultStatusHandler(response -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        logClientError(response);
                        throw ExternalServiceClientException.withDefaultMsg();
                    }
                    if (response.getStatusCode().is5xxServerError()) {
                        logServerError(response);
                        throw ExternalServiceServerException.withDefaultMsg();
                    }

                    return true;
                });
    }

    private static void logClientError(ClientHttpResponse response) throws IOException {
        log.error("Client error with status {} from external service. Response: {}",
                response.getStatusCode(), response);
    }

    private static void logServerError(ClientHttpResponse response) throws IOException {
        log.error("Server error with status {} from external service. Response: {}",
                response.getStatusCode(), response);
    }
}

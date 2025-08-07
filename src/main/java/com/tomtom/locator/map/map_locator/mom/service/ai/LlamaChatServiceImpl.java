package com.tomtom.locator.map.map_locator.mom.service.ai;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@MethodCallLogged
public class LlamaChatServiceImpl implements LlmService {
    private final RestTemplate restTemplate;
    private final String llamaApiUrl;
    private final String modelName;

    public LlamaChatServiceImpl(
            @Value("${spring.ai.local.url}") String llamaApiUrl,
            @Value("${spring.ai.local.model}") String modelName) {
        this.restTemplate = new RestTemplate();
        this.llamaApiUrl = llamaApiUrl;
        this.modelName = modelName;
    }

    @Override
    public String getNameForPoiNames(List<String> names) {
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(getPayload(names), getHeaders());
        try {
            return extractNameFromResponse(restTemplate.postForObject(llamaApiUrl, request, Map.class));
        } catch (Exception e) {
            log.error("Error while calling Llama API: {}", e.getMessage(), e);
            return names.getFirst();
        }
    }

    private Map<String, Object> getPayload(List<String> names) {
        return Map.of(
                "model", modelName,
                "messages", List.of(
                        Map.of("role", "user", "content", "Give me funny name for given list of points of " +
                                "interest: " + names.toString() + ", answer in just one name, do not use any other words," +
                                " give just one name. Respond in format {name}")
                )
        );
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String extractNameFromResponse(Map response) {

        if (response != null && response.containsKey("choices")) {
            Object choices = response.get("choices");
            if (choices instanceof List && !((List<?>) choices).isEmpty()) {
                Object message = ((Map<?, ?>) ((List<?>) choices).getFirst()).get("message");
                if (message instanceof Map) {
                    Object content = ((Map<?, ?>) message).get("content");
                    if (content != null) {
                        return content.toString().replace("{", "").replace("}", "");
                    }
                }
            }
        }

        throw new IllegalStateException("Unexpected response format from Llama API: " + response);
    }


}

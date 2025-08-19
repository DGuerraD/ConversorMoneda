package com.diego.currency.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExchangeRateApiProvider implements CurrencyRateProvider {
    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public ExchangeRateApiProvider(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key requerida para ExchangeRate-API.");
        }
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();
    }

    @Override
    public BigDecimal getRate(String from, String to) throws Exception {
        String url = String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s", apiKey, from.toUpperCase(), to.toUpperCase());
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(12))
                .GET()
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) {
            throw new IOException("HTTP " + res.statusCode() + " al consultar ExchangeRate-API");
        }
        JsonNode json = mapper.readTree(res.body());
        if (json.has("result") && "success".equalsIgnoreCase(json.get("result").asText())) {
            if (json.has("conversion_rate")) {
                return json.get("conversion_rate").decimalValue();
            }
        }
        throw new IOException("Respuesta inválida de ExchangeRate-API: " + res.body());
    }

    @Override
    public String name() {
        return "ExchangeRate-API";
    }
}

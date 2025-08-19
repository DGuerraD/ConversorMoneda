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

public class ExchangerateHostProvider implements CurrencyRateProvider {
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public ExchangerateHostProvider() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();
    }

    @Override
    public BigDecimal getRate(String from, String to) throws Exception {
        // We'll use the /convert endpoint with amount=1 to directly get the rate
        String url = String.format("https://api.exchangerate.host/convert?from=%s&to=%s&amount=1", from.toUpperCase(), to.toUpperCase());
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(12))
                .GET()
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            throw new IOException("HTTP " + res.statusCode() + " al consultar exchangerate.host");
        }
        JsonNode json = mapper.readTree(res.body());
        if (json.has("result")) {
            return json.get("result").decimalValue();
        }
        throw new IOException("Respuesta inválida de exchangerate.host: " + res.body());
    }

    @Override
    public String name() {
        return "exchangerate.host";
    }
}

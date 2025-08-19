package com.diego.currency.config;

import com.diego.currency.api.CurrencyRateProvider;
import com.diego.currency.api.ExchangeRateApiProvider;
import com.diego.currency.api.ExchangerateHostProvider;

public class AppConfig {

    public static CurrencyRateProvider buildProvider() {
        // If the EXCHANGE_API_KEY exists, use ExchangeRate-API, else fallback to exchangerate.host
        String key = System.getenv("EXCHANGE_API_KEY");
        if (key != null && !key.isBlank()) {
            try {
                return new ExchangeRateApiProvider(key);
            } catch (Exception e) {
                System.err.println("No se pudo inicializar ExchangeRate-API, usando fallback: " + e.getMessage());
            }
        }
        return new ExchangerateHostProvider();
    }
}

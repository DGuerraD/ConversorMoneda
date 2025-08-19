package com.diego.currency.service;

import com.diego.currency.api.CurrencyRateProvider;
import com.diego.currency.model.ConversionResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConverterService {

    private final CurrencyRateProvider provider;

    public ConverterService(CurrencyRateProvider provider) {
        this.provider = provider;
    }

    public ConversionResult convert(String from, String to, BigDecimal amount) throws Exception {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto debe ser no negativo.");
        }
        BigDecimal rate = provider.getRate(from, to);
        BigDecimal value = amount.multiply(rate).setScale(4, RoundingMode.HALF_UP);
        return new ConversionResult(from.toUpperCase(), to.toUpperCase(), amount, rate, value, provider.name());
    }
}

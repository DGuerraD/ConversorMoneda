package com.diego.currency.model;

import java.math.BigDecimal;

public record ConversionResult(
        String from,
        String to,
        BigDecimal amount,
        BigDecimal rate,
        BigDecimal value,
        String provider
) {}

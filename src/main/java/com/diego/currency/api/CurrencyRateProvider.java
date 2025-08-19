package com.diego.currency.api;

import java.math.BigDecimal;

public interface CurrencyRateProvider {
    /**
     * Returns the conversion rate: 1 unit of 'from' equals X units of 'to'.
     */
    BigDecimal getRate(String from, String to) throws Exception;

    /**
     * Returns a short provider name for display.
     */
    String name();
}

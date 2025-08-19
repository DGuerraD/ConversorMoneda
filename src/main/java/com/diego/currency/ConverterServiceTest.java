package com.diego.currency;

import com.diego.currency.api.CurrencyRateProvider;
import com.diego.currency.model.ConversionResult;
import com.diego.currency.service.ConverterService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ConverterServiceTest {

    static class StubProvider implements CurrencyRateProvider {
        @Override public BigDecimal getRate(String from, String to) { return new BigDecimal("2.5"); }
        @Override public String name() { return "stub"; }
    }

    @Test
    void convertComputesValue() throws Exception {
        ConverterService svc = new ConverterService(new StubProvider());
        ConversionResult res = svc.convert("USD", "MXN", new BigDecimal("10"));
        assertEquals(new BigDecimal("25.0000"), res.value());
        assertEquals(new BigDecimal("2.5"), res.rate());
        assertEquals("stub", res.provider());
    }

    @Test
    void negativeAmountFails() {
        ConverterService svc = new ConverterService(new StubProvider());
        assertThrows(IllegalArgumentException.class, () -> svc.convert("USD", "MXN", new BigDecimal("-1")));
    }
}

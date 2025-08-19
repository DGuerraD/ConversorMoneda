package com.diego.currency;

import com.diego.currency.util.NumberFormats;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class NumberFormatsTest {

    @Test
    void moneyFormats() {
        String s = NumberFormats.money(new BigDecimal("1234.56"), "USD", Locale.US);
        assertTrue(s.contains("$"));
    }

    @Test
    void decimalFormats() {
        String s = NumberFormats.decimal(new BigDecimal("3.1415926"), 4);
        assertEquals("3.1416", s);
    }
}

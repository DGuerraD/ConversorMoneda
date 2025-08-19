package com.diego.currency.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class NumberFormats {

    public static String money(BigDecimal value, String currencyCode, Locale loc) {
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance(loc);
            nf.setCurrency(Currency.getInstance(currencyCode.toUpperCase()));
            return nf.format(value);
        } catch (Exception e) {
            NumberFormat nf = NumberFormat.getNumberInstance(loc);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            return value + " " + currencyCode.toUpperCase();
        }
    }

    public static String decimal(BigDecimal value, int maxFraction) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(maxFraction);
        nf.setMinimumFractionDigits(0);
        return nf.format(value);
    }
}

package com.diego.currency.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoryEntry(
        LocalDateTime timestamp,
        String from,
        String to,
        BigDecimal amount,
        BigDecimal rate,
        BigDecimal value,
        String provider
) {}

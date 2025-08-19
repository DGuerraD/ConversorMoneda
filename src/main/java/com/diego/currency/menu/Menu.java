package com.diego.currency.menu;

import com.diego.currency.config.AppConfig;
import com.diego.currency.model.ConversionResult;
import com.diego.currency.model.HistoryEntry;
import com.diego.currency.service.ConverterService;
import com.diego.currency.util.ConsoleUtils;
import com.diego.currency.util.NumberFormats;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Menu {

    private final ConverterService converter;
    private final List<HistoryEntry> history;
    private final Scanner scanner;

    public Menu() {
        this.converter = new ConverterService(AppConfig.buildProvider());
        this.history = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        ConsoleUtils.clearScreen();
        System.out.println("============================================");
        System.out.println("   CONVERSOR DE MONEDAS - JAVA (Alura/ONE)  ");
        System.out.println("============================================");
        System.out.println("Bienvenido/a. Elige una opción:\n");

        boolean keep = true;
        while (keep) {
            printOptions();
            int opt = ConsoleUtils.readInt(scanner, "Opción: ");
            switch (opt) {
                case 1 -> quickConvert("USD", "ARS");
                case 2 -> quickConvert("ARS", "USD");
                case 3 -> quickConvert("USD", "BRL");
                case 4 -> quickConvert("BRL", "USD");
                case 5 -> quickConvert("USD", "MXN");
                case 6 -> quickConvert("MXN", "USD");
                case 7 -> freeConvert();
                case 8 -> showHistory();
                case 9 -> exportHistory();
                case 0 -> {
                    System.out.println("¡Hasta luego!");
                    keep = false;
                }
                default -> System.out.println("Opción inválida, intenta nuevamente.");
            }
            if (keep) ConsoleUtils.pause(scanner);
        }
    }

    private void printOptions() {
        System.out.println();
        System.out.println("1) USD → ARS");
        System.out.println("2) ARS → USD");
        System.out.println("3) USD → BRL");
        System.out.println("4) BRL → USD");
        System.out.println("5) USD → MXN");
        System.out.println("6) MXN → USD");
        System.out.println("7) Conversión libre (cualquier par ISO 4217)");
        System.out.println("8) Ver historial");
        System.out.println("9) Exportar historial a CSV");
        System.out.println("0) Salir");
    }

    private void quickConvert(String from, String to) {
        BigDecimal amount = ConsoleUtils.readBigDecimal(scanner, "Ingresa el monto a convertir (" + from + "): ");
        convertAndPrint(from, to, amount);
    }

    private void freeConvert() {
        String from = ConsoleUtils.readCurrency(scanner, "Moneda origen (p. ej. USD, EUR, MXN): ");
        String to = ConsoleUtils.readCurrency(scanner, "Moneda destino (p. ej. ARS, BRL, CLP): ");
        BigDecimal amount = ConsoleUtils.readBigDecimal(scanner, "Monto a convertir: ");
        convertAndPrint(from, to, amount);
    }

    private void convertAndPrint(String from, String to, BigDecimal amount) {
        try {
            ConversionResult result = converter.convert(from, to, amount);
            String amountStr = NumberFormats.money(amount, from, Locale.US);
            String valueStr = NumberFormats.money(result.value(), to, localeFor(to));

            System.out.println();
            System.out.printf("El valor de %s corresponde a: %s%n", amountStr, valueStr);

            history.add(new HistoryEntry(LocalDateTime.now(), from, to, amount, result.rate(), result.value(), result.provider()));

        } catch (Exception e) {
            System.out.println("Error realizando la conversión: " + e.getMessage());
        }
    }

    private void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No hay conversiones en el historial.");
            return;
        }
        System.out.println("\nHISTORIAL:");
        System.out.println("Fecha/Hora | De→A | Monto | Tasa | Resultado | Fuente");
        for (HistoryEntry h : history) {
            System.out.printf("%s | %s→%s | %s | %s | %s | %s%n",
                    h.timestamp(),
                    h.from(), h.to(),
                    NumberFormats.money(h.amount(), h.from(), Locale.US),
                    NumberFormats.decimal(h.rate(), 6),
                    NumberFormats.money(h.value(), h.to(), localeFor(h.to())),
                    h.provider()
            );
        }
    }

    private void exportHistory() {
        if (history.isEmpty()) {
            System.out.println("No hay conversiones en el historial para exportar.");
            return;
        }
        String filename = "historial_conversor.csv";
        try {
            ConsoleUtils.exportHistoryCsv(history, filename);
            System.out.println("Historial exportado a: " + filename);
        } catch (Exception e) {
            System.out.println("No se pudo exportar el historial: " + e.getMessage());
        }
    }

    private Locale localeFor(String currency) {
        // Simple helper for formatting – defaults to es_MX for latam-ish style
        return switch (currency.toUpperCase()) {
            case "ARS" -> new Locale("es", "AR");
            case "BRL" -> new Locale("pt", "BR");
            case "MXN" -> new Locale("es", "MX");
            case "USD" -> Locale.US;
            case "EUR" -> Locale.GERMANY;
            default -> new Locale("es", "MX");
        };
    }
}

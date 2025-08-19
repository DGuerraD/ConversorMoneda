package com.diego.currency.util;

import com.diego.currency.model.HistoryEntry;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUtils {

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ignored) {}
    }

    public static void pause(Scanner scanner) {
        System.out.print("\nPresiona ENTER para continuar...");
        scanner.nextLine();
    }

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido.");
            }
        }
    }

    public static java.math.BigDecimal readBigDecimal(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim().replace(",", ".");
            try {
                return new java.math.BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Ingresa un monto válido (usa punto o coma para decimales).");
            }
        }
    }

    public static String readCurrency(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
            if (s.matches("^[A-Z]{3}$")) return s;
            System.out.println("Código inválido. Debe ser ISO 4217 de 3 letras (p. ej. USD, EUR, MXN).");
        }
    }

    public static void exportHistoryCsv(List<HistoryEntry> history, String filename) throws IOException {
        try (FileWriter w = new FileWriter(filename)) {
            w.write("timestamp,from,to,amount,rate,value,provider\n");
            for (HistoryEntry h : history) {
                w.write(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                        h.timestamp(),
                        h.from(), h.to(),
                        h.amount(),
                        h.rate(),
                        h.value(),
                        h.provider()
                ));
            }
        }
    }
}

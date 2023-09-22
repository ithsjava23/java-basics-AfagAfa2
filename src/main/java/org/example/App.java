package org.example;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
public class App {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("sv", "SE"));
        int[][] elpriser = new int[24][2];
        Scanner scanner = new Scanner(System.in);
        String choice;
        String menu;
        do {
            menu = ("""
                    Elpriser
                    ========
                    1. Inmatning
                    2. Min, Max och Medel
                    3. Sortera
                    4. Bästa Laddningstid (4h)
                    e. Avsluta
                    \n""");
            System.out.print(menu);
            choice = scanner.next();


            switch (choice.toLowerCase()) {
                case "1" -> inmatning(elpriser, scanner);
                case "2" -> beraknaMinMaxMedel(elpriser);
                case "3" -> sortera(elpriser);
                case "4" -> hittaBastaLaddningstid(elpriser);
                case "e" -> {
                    return;
                }
                default -> System.out.print("Ogiltigt val. Försök igen.");

            }
        } while (!choice.equals("e"));
    }

    private static void inmatning(int[][] elpriser, Scanner scanner) {
        for (int hour = 0; hour < 24; hour++) {
            System.out.print("Ange elpris för timme " + hour + " (öre/kWh): ");
            int price = scanner.nextInt();
            elpriser[hour][1] = price;
        }
        System.out.print("Inmatning klar.");
        scanner.nextLine();
    }
    private static void beraknaMinMaxMedel(int[][] elpriser) {
        int minPrice = Integer.MAX_VALUE;
        int maxPrice = Integer.MIN_VALUE;
        double totalPrice = 0.0;
        int minHour = -1;
        int maxHour = -1;
        for (int hour = 0; hour < 24; hour++) {
            int price = elpriser[hour][1];
            if (price < minPrice) {
                minPrice = price;
                minHour = hour;
            }
            if (price > maxPrice) {
                maxPrice = price;
                maxHour = hour;
            }
            totalPrice += price;
        }
        double medelPris = totalPrice / 24;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.getDefault()));

        String minHourR =String.format("%02d-%02d",minHour,minHour +1);
        String maxHourR =String.format("%02d-%02d",maxHour,maxHour +1);
        String formattedMedelPris = decimalFormat.format(medelPris);
        System.out.print("Lägsta pris: " + minHourR + ", " + minPrice + " öre/kWh\n");
        System.out.print("Högsta pris: " + maxHourR + ", " + maxPrice + " öre/kWh\n");
        System.out.print("Medelpris: " + formattedMedelPris + " öre/kWh\n");
    }
    private static void sortera(int[][] elpriser) {

        HourPrice[] hourPrices = new HourPrice[24];
        for (int hour = 0; hour < 24; hour++) {
            hourPrices[hour] = new HourPrice(hour, elpriser[hour][1]);
        }

        Arrays.sort(hourPrices, (a, b) -> Integer.compare(b.price, a.price));
        System.out.print("Elektricitetspriser (sorterade från dyrast till billigast):"+"\n");
        for (HourPrice hp : hourPrices) {
            int hour = hp.hour;
            int nextHour = (hour + 1);
            int price =hp.price;
            System.out.print(String.format("%02d-%02d %d öre", hour, nextHour,price)+"\n");
        }
    }
    private static void hittaBastaLaddningstid(int[][] elpriser) {
        double lowestTotalPrice = Double.MAX_VALUE;
        int bestStartHour = -1;
        for (int hour = 0; hour < 20; hour++) {
            double totalPrice = 0.0;
            for (int i = 0; i < 4; i++) {
                totalPrice += elpriser[(hour + i) % 24][1];
            }
            if (totalPrice < lowestTotalPrice) {
                lowestTotalPrice = totalPrice;
                bestStartHour = hour;
            }
        }
        double medelPris = lowestTotalPrice / 4;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0", new DecimalFormatSymbols(Locale.getDefault()));
        String formattedMedelPris = decimalFormat.format(medelPris);
        System.out.print("Påbörja laddning klockan " + bestStartHour +"\n");
        System.out.print("Medelpris 4h: " + formattedMedelPris + " öre/kWh\n");
    }
    static class HourPrice {
        int hour;
        int price;
        public HourPrice(int hour, int price) {
            this.hour = hour;
            this.price = price;
        }
    }
}
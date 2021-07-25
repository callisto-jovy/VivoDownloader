package net.bplaced.abzzezz.vivodownloader.util;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConsoleUtil {

    public static final char[] CHARS = new char[]{'|', '/', '-', '\\'};

    public static void displayProgressbar(final int progress, final int total) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < (((double) progress / total) * 100.); i++) {
            builder.append('#');
            System.out.print("[" + builder + "] " + i + "% " + CHARS[i % 4] + "\r");
        }
    }

    public static void displayProgressbar(final Predicate<Integer> integerPredicate) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; integerPredicate.test(i); i++) {
            builder.append('#');
            System.out.print("[" + builder + "] " + CHARS[i % 4] + "\r");
        }
    }

    public static void list(final String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            System.out.printf("(%d) %s%n", i, strings[i]);
        }
    }


    public static void createOptionsMenu(final String[] options, final Consumer<Integer> selectedOption) {
        list(options);
        final Scanner scanner = new Scanner(System.in);
        selectedOption.accept(Integer.parseInt(scanner.next("[+-]?[0-9]+")));
    }
}

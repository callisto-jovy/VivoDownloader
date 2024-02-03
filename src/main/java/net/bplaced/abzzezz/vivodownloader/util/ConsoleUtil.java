package net.bplaced.abzzezz.vivodownloader.util;

import java.io.*;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConsoleUtil {

    public static final char[] CHARS = new char[]{'|', '/', '-', '\\'};

    private final BufferedReader br;
    private final PrintStream pw;

    public ConsoleUtil(final InputStream inputStream, final PrintStream printStream) {
        this.br = new BufferedReader(new InputStreamReader(inputStream));
        this.pw = printStream;
    }

    public void destroy() {
        try {
            pw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine(final String fmt, Object... args) {
        System.out.printf(fmt, args);
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static void println(final String msg) {
        System.out.println(msg);
    }

    public void displayProgressbar(final int progress, final int total) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < (((double) progress / total) * 100.); i++) {
            builder.append('#');
            System.out.print("[" + builder + "] " + i + "% " + CHARS[i % 4] + "\r");
        }
    }

    public void displayProgressbar(final Predicate<Integer> integerPredicate) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; integerPredicate.test(i); i++) {
            builder.append('#');
            System.out.print("[" + builder + "] " + CHARS[i % 4] + "\r");
        }
    }

    public void list(final String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            System.out.printf("(%d) %s%n", i, strings[i]);
        }
    }

    public void createOptionsMenu(final String[] options, final Consumer<Integer> selectedOption) {
        list(options);
        try {
            selectedOption.accept(Integer.parseInt(br.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean option(final String option) {
        return readLine(option + "y/n").equalsIgnoreCase("y");
    }
}

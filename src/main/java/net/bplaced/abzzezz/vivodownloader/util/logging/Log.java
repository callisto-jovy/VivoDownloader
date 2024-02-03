package net.bplaced.abzzezz.vivodownloader.util.logging;

import java.util.Locale;

public class Log<T> {

    public static void info(final String message) {
        log(LogState.INFO, message);
    }

    public static <T> void info(final T message) {
        log(LogState.INFO, message);
    }

    public static void error(final String message) {
        log(LogState.ERROR, message);
    }

    public static <T> void error(final T message) {
        log(LogState.ERROR, message);
    }

    public static void failed(final String message) {
        log(LogState.FAILED, message);
    }

    public static <T> void failed(final T message) {
        log(LogState.FAILED, message);
    }

    public static void fatal(final String message) {
        log(LogState.FATAL, message);
    }

    public static <T> void fatal(final T message) {
        log(LogState.FATAL, message);
    }

    public static void warning(final String message) {
        log(LogState.WARNING, message);
    }

    public static <T> void warning(final T message) {
        log(LogState.WARNING, message);
    }

    public static void other(final String message) {
        log(LogState.OTHER, message);
    }

    public static <T> void other(final T message) {
        log(LogState.OTHER, message);
    }

    public static void log(final LogState logState, final String msg) {
        System.out.println(logState.toString().toUpperCase(Locale.ROOT) + "> " + msg);
    }

    public static <T> void log(final LogState logState, final T msg) {
        log(logState, msg.toString());
    }
}

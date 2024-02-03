package net.bplaced.abzzezz.vivodownloader.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class URLUtil {
    public static URLConnection createURLConnection(final String urlIn, final int connectionTimeout, final int readTimeout, final String[]... requestProperties) throws IOException {
        return createURLConnection(new URL(urlIn), connectionTimeout, readTimeout, requestProperties);
    }

    public static URLConnection createURLConnection(final URL urlIn, final int connectionTimeout, final int readTimeout, final String[]... requestProperties) throws IOException {
        final URLConnection connection = urlIn.openConnection();
        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(connectionTimeout);
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }


    /**
     * Creates a HTTPS URL Connection
     *
     * @param urlIn             url connection url
     * @param requestMethod     requestMethod
     * @param requestProperties request property array to be used
     * @return return pre configured connection
     * @throws IOException url invalid
     */
    public static HttpsURLConnection createHTTPSURLConnection(final String urlIn, final String requestMethod, final String[]... requestProperties) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) new URL(urlIn).openConnection();
        connection.setRequestMethod(requestMethod);
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }

    /**
     * Creates a HTTP URL Connection
     *
     * @param urlIn             url connection url
     * @param requestMethod     requestMethod
     * @param requestProperties request property array to be used
     * @return return pre configured connection
     * @throws IOException url invalid
     */
    public static HttpURLConnection createHTTPURLConnection(final String urlIn, final String requestMethod, final String[]... requestProperties) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(urlIn).openConnection();
        connection.setRequestMethod(requestMethod);
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }

    /**
     * Creates a HTTPS URL connection
     *
     * @param urlIn             url connection url
     * @param requestProperties request property array to be used
     * @return returns a pre configured https url connection
     * @throws IOException url invalid
     */
    public static HttpsURLConnection createHTTPSURLConnection(final String urlIn, final String[]... requestProperties) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) new URL(urlIn).openConnection();
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }

    /**
     * Creates a HTTP URL connection
     *
     * @param urlIn             url connection url
     * @param requestProperties request property array to be used
     * @return returns a pre configured http url connection
     * @throws IOException url invalid
     */
    public static HttpURLConnection createHTTPURLConnection(final String urlIn, final String[]... requestProperties) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(urlIn).openConnection();
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }

    /**
     * Creates a HTTPS URL connection
     *
     * @param urlIn             url connection url
     * @param connectionTimeout connection timeout
     * @param readTimeout       read timeout
     * @param requestProperties request property array to be used
     * @return returns a pre configured http url connection
     * @throws IOException url invalid
     */
    public static HttpsURLConnection createHTTPSURLConnection(final String urlIn, final int connectionTimeout, final int readTimeout, final String[]... requestProperties) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) new URL(urlIn).openConnection();
        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(connectionTimeout);
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }

    /**
     * Creates a HTTPS URL connection
     *
     * @param urlIn             url connection url
     * @param connectionTimeout connection timeout
     * @param readTimeout       read timeout
     * @param requestProperties request property array to be used
     * @return returns a pre configured http url connection
     * @throws IOException url invalid
     */
    public static HttpURLConnection createHTTPURLConnection(final String urlIn, final int connectionTimeout, final int readTimeout, final String[]... requestProperties) throws IOException {
        return createHTTPURLConnection(new URL(urlIn), connectionTimeout, readTimeout, requestProperties);
    }

    public static HttpURLConnection createHTTPSURLConnection(final URL urlIn, final int connectionTimeout, final int readTimeout, final String[]... requestProperties) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) urlIn.openConnection();
        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(connectionTimeout);
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }

    public static HttpURLConnection createHTTPURLConnection(final URL urlIn, final int connectionTimeout, final int readTimeout, final String[]... requestProperties) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) urlIn.openConnection();
        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(connectionTimeout);
        for (final String[] requestProperty : requestProperties) {
            connection.setRequestProperty(requestProperty[0], requestProperty[1]);
        }
        return connection;
    }

    /**
     * Joins all the lines read from an url together
     *
     * @param src    url to read from
     * @param joiner String to join all read lines together
     * @return all joined lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String collectLines(final URLConnection src, final String joiner) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(src.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line).append(joiner);
        }
        bufferedReader.close();
        return builder.toString();
    }

    /**
     * Joins all the lines read from an url together
     *
     * @param src    url to read from
     * @param joiner String to join all read lines together
     * @return all joined lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String collectLines(final URL src, final String joiner) throws IOException {
        return collectLines(src.openConnection(), joiner);
    }

    /**
     * Joins all the lines read from an url together
     *
     * @param src url to read from
     * @return all joined lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String collectLines(final URLConnection src) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(src.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }
        bufferedReader.close();
        return builder.toString();
    }

    /**
     * Joins all the lines read from an url together
     *
     * @param src url to read from
     * @return all joined lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String collectLines(final URL src) throws IOException {
        return collectLines(src.openConnection());
    }

    /**
     * Joins all the lines read from an url together using a stream
     *
     * @param src    url to read from
     * @param joiner String to join all read lines together
     * @return all joined lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String collectLinesStream(final URLConnection src, final String joiner) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(src.getInputStream()));
        try (final Stream<String> lines = bufferedReader.lines()) {
            return lines.collect(Collectors.joining(joiner));
        }
    }

    /**
     * Collects all lines read from an url to an array
     *
     * @param src url to read from
     * @return String array with all the lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String[] collectArray(final URLConnection src) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(src.getInputStream()));
        try (final Stream<String> lines = bufferedReader.lines()) {
            return lines.toArray(String[]::new);
        }
    }

    /**
     * Collects all lines read from an url to an array
     *
     * @param src url to read from
     * @return String array with all the lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String[] collectArray(final URL src) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(src.openStream()));
        try (final Stream<String> lines = bufferedReader.lines()) {
            return lines.toArray(String[]::new);
        }
    }

    /**
     * Collects all lines read from an url to a list
     *
     * @param src url to read from
     * @return String list with all the lines
     * @throws IOException if reader / url fails, etc.
     */
    public static List<String> collectList(final URL src) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(src.openStream()));
        try (final Stream<String> lines = bufferedReader.lines()) {
            return lines.collect(Collectors.toList());
        }
    }

    /**
     * Collects all lines read from an url to a list
     *
     * @param src url to read from
     * @return String list with all the lines
     * @throws IOException if reader / url fails, etc.
     */
    public static List<String> collectList(final URLConnection src) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(src.getInputStream()));
        try (final Stream<String> lines = bufferedReader.lines()) {
            return lines.collect(Collectors.toList());
        }
    }

    /**
     * Joins all the lines read from an url together using a stream
     *
     * @param src    url to read from
     * @param joiner String to join all read lines together
     * @return all joined lines
     * @throws IOException if reader / url fails, etc.
     */
    public static String collectLinesStream(final URL src, final String joiner) throws IOException {
        return collectLinesStream(src.openConnection(), joiner);
    }

    /**
     * Copies file from url
     *
     * @param src  to copy from
     * @param dest destination to copy to
     * @throws IOException @
     */
    public static void copyFileFromURL(final URL src, final File dest) throws IOException {
        copyFileFromURL(src.openConnection(), dest);
    }

    /**
     * Copies file from url
     *
     * @param src  to copy from
     * @param dest destination to copy to
     * @throws IOException @
     */
    public static void copyFileFromURL(final URLConnection src, final File dest) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.getChannel().transferFrom(Channels.newChannel(src.getInputStream()), 0, Long.MAX_VALUE);
        fileOutputStream.close();
    }


    public static void copyFileFromRBC(final ReadableByteChannel readableByteChannel, FileOutputStream outputStream) throws IOException {
        outputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        outputStream.close();
    }
}

package net.bplaced.abzzezz.vivodownloader;

import net.bplaced.abzzezz.vivodownloader.util.ConsoleUtil;
import net.bplaced.abzzezz.vivodownloader.util.Decoder;
import net.bplaced.abzzezz.vivodownloader.util.RBCWrapper;
import net.bplaced.abzzezz.vivodownloader.util.URLUtil;
import net.bplaced.abzzezz.vivodownloader.util.cache.CacheUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainClass {

    public static final MainClass INSTANCE = new MainClass();

    public final Decoder decoder;

    private int progress;

    private final CacheUtil cacheUtil = new CacheUtil(new File(System.getProperty("user.home"), "Vivo-Downloader"));

    public MainClass() {
        this.decoder = new Decoder();
    }

    public static void main(final String[] args) {
        MainClass.INSTANCE.start(args);
    }

    /**
     * Takes in parameters (url and output)
     *
     * @param args arguments
     */
    private void start(final String[] args) {
        cacheUtil.loadCache();

        boolean passedArguments;

        if (args.length < 3 && System.in == null) {
            System.out.println("Not enough arguments specified");
            passedArguments = true;
        } else passedArguments = false;

        final Scanner consoleScanner = new Scanner(System.in);

        System.out.println("Enter Vivo.sx url:");

        final String inputURL = consoleScanner.nextLine().trim();

        boolean hasCachedDir = cacheUtil.cached("dir");


        if (!hasCachedDir) {
            System.out.println("Enter output directory");
            cacheUtil.cacheObject("dir", consoleScanner.nextLine());
            cacheUtil.flushCache();
        }

        final String outputPath = passedArguments ? args[1] : cacheUtil.getString("dir");

        final File output = new File(outputPath);

        System.out.println("Enter filename:");
        final String fileName = passedArguments ? args[2] : consoleScanner.nextLine();

        try {
            final String decodedUrl = decoder.decodeVivo(passedArguments ? args[0] : inputURL);
            final long startTime = System.currentTimeMillis();

            System.out.println("URL decoded: " + decodedUrl);

            final HttpsURLConnection connection = URLUtil.createHTTPSURLConnection(decodedUrl);

            int totalSize = connection.getContentLength();

            final FileOutputStream fileOutputStream = new FileOutputStream(new File(output, fileName));

            URLUtil.copyFileFromRBC(new RBCWrapper(
                    Channels.newChannel(connection.getInputStream()),
                    integer -> {
                        progress += (integer);
                        ConsoleUtil.displayProgressbar(progress, totalSize);
                    }
            ), fileOutputStream);


            final long finalTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);

            System.out.printf("Done downloading file %s from %s. This took %d seconds.", fileName, decodedUrl, finalTime);
        } catch (IOException e) {
            System.out.println("Could not decode vivo.sx url");
            e.printStackTrace();
        }
    }
}

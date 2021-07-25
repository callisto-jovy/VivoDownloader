package net.bplaced.abzzezz.vivodownloader.util;

import net.bplaced.abzzezz.vivodownloader.util.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decoder {

    public String decodeVivo(final String url) throws IOException {
        System.out.println("Decoding vivo direct link");
        final StringBuilder finalUrl = new StringBuilder();
        final Pattern pattern = Pattern.compile("Core\\.InitializeStream\\s*\\(\\s*\\{[^)}]*source\\s*:\\s*'(.*?)',");
        final Document document = Jsoup.connect(url).userAgent(Constant.USER_AGENT).parser(Parser.xmlParser()).get();
        final Element body = document.body();

        final Optional<Element> sourceOptional = body.getElementsByClass("vivo-website-wrapper")
                .first()
                .getElementsByTag("script")
                .stream()
                .filter(element -> element.ownText().startsWith("Core"))
                .findAny();


        if (sourceOptional.isPresent()) {
            System.out.println("Source present");
            String source = sourceOptional.get().ownText();

            final Matcher matcher = pattern.matcher(source);
            if (matcher.find()) {
                source = matcher.group(1);
                source = URLDecoder.decode(source, StandardCharsets.UTF_8.toString());

                for (int i = 0; i < source.toCharArray().length; i++) {
                    char c = source.charAt(i);
                    if (c != ' ') {
                        c += '/';
                        if (126 < c) {
                            c -= 94;
                        }
                        finalUrl.append(c);
                    }
                }
            } else System.out.println("No pattern found!");
        } else System.out.println("No optional present");

        return finalUrl.toString();
    }
}

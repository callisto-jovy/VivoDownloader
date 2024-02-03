package net.bplaced.abzzezz.vivodownloader.util;

import java.util.regex.Pattern;

public class Constant {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; rv:20.0) Gecko/20121202 Firefox/20.0";

    public static final Pattern OWN_FORMAT_PATTERN = Pattern.compile("^[f]\"(.+)[\"]$", Pattern.MULTILINE);
}

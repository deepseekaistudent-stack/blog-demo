package com.example.lianxi.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * HTML 清洗工具，基于 Jsoup。
 * cleanComment 清除所有HTML（Safelist.none）。
 */
public class HtmlSanitizer {
    public static String cleanComment(String input) {
        if (input == null) return null;
        return Jsoup.clean(input, Safelist.none());
    }

}

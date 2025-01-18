package com.example.kino_search.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static final String BUNDLE_NAME = "i18n.messages"; // Путь без расширения .properties
    private static Locale currentLocale = Locale.ENGLISH;

    public static void setLocale(String language) {
        currentLocale = new Locale(language);
    }

    public static String getMessage(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        return bundle.getString(key);
    }
}

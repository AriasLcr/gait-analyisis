package gait.api.utils;

import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@UtilityClass
public class HtmlSanitizer {
    public static String sanitizeHtml(String html) {
        return Jsoup.clean(html, Safelist.basic());
    }
}

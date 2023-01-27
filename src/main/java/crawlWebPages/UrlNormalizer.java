package crawlWebPages;

public class UrlNormalizer {

    public String normalize(String url) {
        if (!(url.startsWith("www") || url.startsWith("http"))) {
            return null;
        }
        String normalizedUrl = url.toLowerCase();

        if (normalizedUrl.startsWith("www")) {
            normalizedUrl = "https://" + normalizedUrl;
        }
        if (normalizedUrl.contains("#")) {
            normalizedUrl = normalizedUrl.substring(0, normalizedUrl.indexOf('#'));
        }
        if (normalizedUrl.endsWith("/")) {
            normalizedUrl = normalizedUrl.substring(0, normalizedUrl.length() - 1);
        }
        return normalizedUrl;
    }
}

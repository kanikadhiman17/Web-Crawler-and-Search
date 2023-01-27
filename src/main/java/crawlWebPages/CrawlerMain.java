package crawlWebPages;

import java.io.IOException;

public class CrawlerMain {

    public static void main (String[] args) throws IOException {
        String seed = "https://www.cc.gatech.edu/";
        Integer depth = 30;

        Crawler crawler = new Crawler();
        crawler.startCrawl(seed, depth);
    }
}

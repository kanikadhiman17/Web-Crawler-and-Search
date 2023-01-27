package crawlWebPages;

import com.opencsv.CSVWriter;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class Crawler {

    private int crawlDepth;
    HashSet<String> crawledPages;
    Integer LIMIT = 1601;
    Integer currCount = 0;
    BufferedWriter mainFile = null;
    Long startTime = null;
    CSVWriter csvWriter = null;

    public void startCrawl(String seed, Integer depth) throws IOException {
        initiateVariables(depth);

        UrlNormalizer normalizer = new UrlNormalizer();
        String normalizedUrl = normalizer.normalize(seed);

        crawl(normalizedUrl, 0);

        csvWriter.close();
        mainFile.close();
    }

    private void initiateVariables(Integer depth) throws IOException {
        this.crawlDepth = depth;
        this.crawledPages = new HashSet<>();

        createDirectoryIfNotExists(System.getProperty("user.dir") + "/webPages");
        this.mainFile = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/webPages/metadata.txt"));
        this.startTime = System.currentTimeMillis();

        createDirectoryIfNotExists(System.getProperty("user.dir") + "/statistics");
        this.csvWriter = new CSVWriter(new FileWriter(System.getProperty("user.dir") + "/statistics/crawlerStats.csv"));
        String[] line = {"Count", "Time in Seconds"};
        csvWriter.writeNext(line);
        csvWriter.flush();
    }

    private void crawl(String url, int depth) throws IOException {
        System.out.println("Crawling " + url + " at depth " + depth + ", count: " + (currCount + 1));
        UrlNormalizer normalizer = new UrlNormalizer();

        Document doc = connectAndGetDoc(url);

        if (doc != null) {
            currCount++;
            this.crawledPages.add(url);
            writeToFile(url, doc);

            if(this.currCount>=this.LIMIT)
                System.exit(0);

            if (depth < this.crawlDepth) {
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String normalizedUrl = normalizer.normalize(link.absUrl("href"));
                    if (normalizedUrl != null && !this.crawledPages.contains(normalizedUrl)) {
                        crawl(normalizedUrl, depth + 1);
                    }
                }
            }
        }
    }

    private void writeToFile(String url, Document doc) throws IOException {
        int folderNum = (this.currCount/100) + 1;
        if(currCount%100==0){
            folderNum--;
            Long endTime = System.currentTimeMillis();
            String[] data = {String.valueOf(this.currCount), String.valueOf((endTime-this.startTime)/1000)};
            csvWriter.writeNext(data);
            csvWriter.flush();
        }

        String directoryPath = System.getProperty("user.dir") + "/webPages/folder-"+ folderNum;
        createDirectoryIfNotExists(directoryPath);

        BufferedWriter bfWriter = new BufferedWriter(new FileWriter( directoryPath+"/page-"+ currCount +".txt"));
        String parsedContents = doc.body().text();
        bfWriter.write(parsedContents);
        bfWriter.flush();
        bfWriter.close();

        mainFile.write("Count: "+ currCount + ", URL: " + url);
        mainFile.newLine();
        mainFile.flush();
    }

    private Document connectAndGetDoc(String url){
        Document doc = null;
        try {
            Connection con = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36")
                    .ignoreHttpErrors(true)
                    .timeout(20000);

            Connection.Response response = con.execute();
            if (response.statusCode() == 200) {
                doc = con.get();
            }
        } catch (HttpStatusException e) {
            System.out.println("URL could not be parsed. " + e);
        } catch (Exception e) {
            System.out.println("Jsoup exception while connecting to url: " + url + ". " + e);
        }
        return doc;
    }

    private void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}

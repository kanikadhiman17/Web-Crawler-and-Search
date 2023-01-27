package createIndexes;

import com.opencsv.CSVWriter;
import searchKeywords.SearchKeyword;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class IndexMain {

    private static final String STANDARD_ANALYSER = "standard";
    private static final String SIMPLE_ANALYSER = "simple";
    private static final String STOP_ANALYSER = "stop";
    private static final String WHITESPACE_ANALYSER = "whitespace";
    private static CSVWriter csvWriter = null;
    private static Long startTime = null;
    private final static String WEB_PAGES_PATH = System.getProperty("user.dir") + "/webPages";


    static {
        try {
            createDirectoryIfNotExists(System.getProperty("user.dir") + "/statistics");
            csvWriter = new CSVWriter(new FileWriter(System.getProperty("user.dir") + "/statistics/indexStats.csv", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CreateIndex createIndex = new CreateIndex();

        //initial100PagesExecutor(createIndex);
        //onePageExecutor(createIndex);
        //oneHundredPageExecutor(createIndex);
        //twoHundredPageExecutor(createIndex);
        //threeHundredPageExecutor(createIndex);
        //fourHundredPageExecutor(createIndex);
        //fiveHundredPageExecutor(createIndex);

        csvWriter.close();
    }

    private static void initial100PagesExecutor(CreateIndex createIndex) throws IOException, InterruptedException {
        String[] header = {"Analyzer", "Num of Records Indexed" , "Time Taken", "Num of Keywords"};
        csvWriter.writeNext(header);
        csvWriter.flush();
        initial100Pages(createIndex, STANDARD_ANALYSER);
        initial100Pages(createIndex, SIMPLE_ANALYSER);
        initial100Pages(createIndex, STOP_ANALYSER);
        initial100Pages(createIndex, WHITESPACE_ANALYSER);
    }

    private static void initial100Pages(CreateIndex createIndex, String analyser) throws IOException, InterruptedException {
        startTime = System.currentTimeMillis();
        createIndex.createIndex(analyser, 1, 1);
        recordTimeAndKeywords(analyser, 1, 1);
    }

    private static void onePageExecutor(CreateIndex createIndex) throws IOException, InterruptedException {
        onePage(createIndex, STANDARD_ANALYSER);
        onePage(createIndex, SIMPLE_ANALYSER);
        onePage(createIndex, STOP_ANALYSER);
        onePage(createIndex, WHITESPACE_ANALYSER);
    }

    private static void onePage(CreateIndex createIndex, String analyser) throws IOException, InterruptedException {
        startTime = System.currentTimeMillis();
        createIndex.createIndex(analyser, 17, 17);
        recordTimeAndKeywords(analyser, 17,17);
    }

    private static void oneHundredPageExecutor(CreateIndex createIndex) throws IOException, InterruptedException {
        oneHundredPage(createIndex, STANDARD_ANALYSER);
        oneHundredPage(createIndex, SIMPLE_ANALYSER);
        oneHundredPage(createIndex, STOP_ANALYSER);
        oneHundredPage(createIndex, WHITESPACE_ANALYSER);
    }

    private static void oneHundredPage(CreateIndex createIndex, String analyser) throws IOException, InterruptedException {
        startTime = System.currentTimeMillis();
        createIndex.createIndex(analyser, 2, 2);
        recordTimeAndKeywords(analyser, 2,2);
    }

    private static void twoHundredPageExecutor(CreateIndex createIndex) throws IOException, InterruptedException {
        twoHundredPage(createIndex, STANDARD_ANALYSER);
        twoHundredPage(createIndex, SIMPLE_ANALYSER);
        twoHundredPage(createIndex, STOP_ANALYSER);
        twoHundredPage(createIndex, WHITESPACE_ANALYSER);
    }

    private static void twoHundredPage(CreateIndex createIndex, String analyser) throws IOException, InterruptedException {
        startTime = System.currentTimeMillis();
        createIndex.createIndex(analyser, 3, 4);
        recordTimeAndKeywords(analyser, 3,4);
    }

    private static void threeHundredPageExecutor(CreateIndex createIndex) throws IOException, InterruptedException {
        threeHundredPage(createIndex, STANDARD_ANALYSER);
        threeHundredPage(createIndex, SIMPLE_ANALYSER);
        threeHundredPage(createIndex, STOP_ANALYSER);
        threeHundredPage(createIndex, WHITESPACE_ANALYSER);
    }

    private static void threeHundredPage(CreateIndex createIndex, String analyser) throws IOException, InterruptedException {
        startTime = System.currentTimeMillis();
        createIndex.createIndex(analyser, 5, 7);
        recordTimeAndKeywords(analyser, 5,7);
    }

    private static void fourHundredPageExecutor(CreateIndex createIndex) throws IOException, InterruptedException {
        fourHundredPage(createIndex, STANDARD_ANALYSER);
        fourHundredPage(createIndex, SIMPLE_ANALYSER);
        fourHundredPage(createIndex, STOP_ANALYSER);
        fourHundredPage(createIndex, WHITESPACE_ANALYSER);
    }

    private static void fourHundredPage(CreateIndex createIndex, String analyser) throws IOException, InterruptedException {
        startTime = System.currentTimeMillis();
        createIndex.createIndex(analyser, 8, 11);
        recordTimeAndKeywords(analyser, 8,11);
    }

    private static void fiveHundredPageExecutor(CreateIndex createIndex) throws IOException, InterruptedException {
        fiveHundredPage(createIndex, STANDARD_ANALYSER);
        fiveHundredPage(createIndex, SIMPLE_ANALYSER);
        fiveHundredPage(createIndex, STOP_ANALYSER);
        fiveHundredPage(createIndex, WHITESPACE_ANALYSER);
    }

    private static void fiveHundredPage(CreateIndex createIndex, String analyser) throws IOException, InterruptedException {
        startTime = System.currentTimeMillis();
        createIndex.createIndex(analyser, 12, 16);
        recordTimeAndKeywords(analyser, 12,16);
    }

    private static void recordTimeAndKeywords(String analyser, Integer startPage, Integer lastPage) throws IOException, InterruptedException {
        long endTime = System.currentTimeMillis();
        SearchKeyword searchKeyword = new SearchKeyword();

        long docsEntered = 0;
        for(int i=startPage;i<=lastPage;i++){
            String path = WEB_PAGES_PATH + "/folder-" + i;
            try (Stream<Path> files = Files.list(Paths.get(path))) {
                docsEntered += files.count();
            }
        }
        int duration = (int) (endTime - startTime);
        long numKeywords = searchKeyword.keywordSize(analyser);
        System.out.println("Keywords in " + analyser + " analyzer: " + numKeywords);
        String[] data = {analyser, Long.toString(docsEntered), Integer.toString(duration), Long.toString(numKeywords)};
        csvWriter.writeNext(data);
        csvWriter.flush();
    }

    public static void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

}

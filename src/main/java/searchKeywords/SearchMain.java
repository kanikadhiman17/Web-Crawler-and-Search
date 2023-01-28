package searchKeywords;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

public class SearchMain {

    private static final String STANDARD_ANALYSER = "standard";
    private static final String SIMPLE_ANALYSER = "simple";
    private static final String STOP_ANALYSER = "stop";
    private static final String WHITESPACE_ANALYSER = "whitespace";

    public static void main(String[] args) throws IOException, ParseException {
        String keywordSearch = "computer";
        Integer n = 20;

//        getTopNKeywords(n, STANDARD_ANALYSER);
//        getTopNKeywords(n, SIMPLE_ANALYSER);
//        getTopNKeywords(n, STOP_ANALYSER);
//        getTopNKeywords(n, WHITESPACE_ANALYSER);

        long startTime = System.currentTimeMillis();
        searchKeyword(keywordSearch, STANDARD_ANALYSER);
        searchKeyword(keywordSearch, SIMPLE_ANALYSER);
        searchKeyword(keywordSearch, STOP_ANALYSER);
        searchKeyword(keywordSearch, WHITESPACE_ANALYSER);
        System.out.println("Time taken to search keyword: " + (System.currentTimeMillis() - startTime) + "ms");

//        getKeywordSize(STANDARD_ANALYSER);
//        getKeywordSize(SIMPLE_ANALYSER);
//        getKeywordSize(STOP_ANALYSER);
//        getKeywordSize(WHITESPACE_ANALYSER);
    }


    private static void getTopNKeywords(Integer n, String analyser) throws IOException, ParseException {
        SearchKeyword searchKeyword = new SearchKeyword();

        List<String> topNKeywords = searchKeyword.topNKeywords(n, analyser);
        System.out.println("Top " + n + " keywords in "+ analyser + " analyzer: " + topNKeywords);

        System.out.println("Completed the execution for " + analyser + " analyzer");
        System.out.println();
    }

    private static void getKeywordSize(String analyser) throws IOException {
        SearchKeyword searchKeyword = new SearchKeyword();
        Long keywordSize = searchKeyword.keywordSize(analyser);
        System.out.println("Size of keywords for " + analyser + " analyzer is: " + keywordSize);
    }

    private static void searchKeyword(String keywordSearch, String analyser) throws IOException, ParseException {
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.searchKeyword(keywordSearch, analyser);

        System.out.println("Completed the execution for " + analyser + " analyzer");
        System.out.println();
    }
}

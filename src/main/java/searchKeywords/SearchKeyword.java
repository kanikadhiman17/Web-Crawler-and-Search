package searchKeywords;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SearchKeyword {
    private final String INDEX_PATH = System.getProperty("user.dir") + "/indexes";
    private final String CONTENTS = "contents";
    private final String PATH = "path";
    private final Integer MAX_RESULTS = 10;

    public void searchKeyword(String keyword, String analyser) throws IOException, ParseException {
        Directory directory = getDirectory(analyser);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        TopDocs foundDocs = searchInContent(keyword, searcher);
        System.out.println("Total Results :: " + foundDocs.totalHits);

        for (ScoreDoc sd : foundDocs.scoreDocs) {
            Document d = searcher.doc(sd.doc);
            String webPage = d.get(PATH);
            String url = null;
            try (Stream<String> lines = Files.lines(Paths.get(System.getProperty("user.dir") + "/webPages/metadata.txt"))) {
                String number = webPage.substring(webPage.lastIndexOf("-") + 1);
                number = number.substring(0, number.lastIndexOf("."));
                int num = Integer.parseInt(number);
                Optional<String> optUrl = lines.skip(num-1).findFirst();
                if(optUrl.isPresent()) {
                    url = optUrl.get().substring(optUrl.get().indexOf("URL:") +5);
                    System.out.println(url);
                }
            }
            System.out.println("Path :: "+ getPath(d.get(PATH)) + ", Score : " + sd.score);
        }
    }

    private String getPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private TopDocs searchInContent(String keyword, IndexSearcher searcher) throws ParseException, IOException {
        QueryParser qp = new QueryParser(CONTENTS, new StandardAnalyzer());
        Query query = qp.parse(keyword);
        return searcher.search(query, MAX_RESULTS);
    }

    public List<String> topNKeywords(int n, String analyser) throws IOException {
        List<String> topNKeywords = new ArrayList<>();
        Directory dir = getDirectory(analyser);
        IndexReader reader = DirectoryReader.open(dir);

        Fields fields = MultiFields.getFields(reader);
        final Iterator<String> iterator = fields.iterator();

        while(iterator.hasNext() && n>0) {
            final String field = iterator.next();
            final Terms terms = MultiFields.getTerms(reader, field);
            final TermsEnum it = terms.iterator();
            BytesRef term = it.next();
            while (term != null && n>0) {
                topNKeywords.add(term.utf8ToString());
                term = it.next();
                n--;
            }
        }
        return topNKeywords;
    }

    public Long keywordSize(String analyser) throws IOException {
        Directory dir = getDirectory(analyser);
        IndexReader reader = DirectoryReader.open(dir);
        long count = 0L;

        Fields fields = MultiFields.getFields(reader);
        for (String field : fields) {
            final Terms terms = MultiFields.getTerms(reader, field);
            final TermsEnum it = terms.iterator();
            BytesRef term = it.next();
            while (term != null) {
                count++;
                term=it.next();
            }
        }
        return count;
    }

    private String getAnalyserClassName(String analyser){
        Analyzer analyzer = switch (analyser) {
            case "whitespace" -> new WhitespaceAnalyzer();
            case "stop" -> new StopAnalyzer();
            case "simple" -> new SimpleAnalyzer();
            default -> new StandardAnalyzer();
        };
        return analyzer.getClass().getSimpleName();
    }

    private Directory getDirectory(String analyser) throws IOException {
        return FSDirectory.open(Paths.get(INDEX_PATH + "/" + getAnalyserClassName(analyser)));
    }
}

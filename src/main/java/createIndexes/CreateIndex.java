package createIndexes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CreateIndex {

    private final String INPUT_PATH = System.getProperty("user.dir") + "/webPages";
    private final String INDEX_PATH = System.getProperty("user.dir") + "/indexes";

    public void createIndex(String analyserStr, Integer startFolder, Integer endFolder) throws IOException {
        Analyzer analyzer = switch (analyserStr) {
            case "whitespace" -> new WhitespaceAnalyzer();
            case "stop" -> new StopAnalyzer();
            case "simple" -> new SimpleAnalyzer();
            default -> new StandardAnalyzer();
        };

        for(int i=startFolder; i<=endFolder; i++) {
            String folderName = "folder-" + i;
            String inputPath = INPUT_PATH + "/" + folderName;
            createIndexForFolder(analyzer, inputPath);
        }
    }

    private void createIndexForFolder(Analyzer analyzer, String inputPath) throws IOException {
        final Path docDir = Paths.get(inputPath);
        String analyser = analyzer.getClass().getSimpleName();
        Directory directory = FSDirectory.open(Paths.get(INDEX_PATH + "/" + analyser));
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(directory, iwc);
        indexDocuments(writer, docDir);
        writer.flush();
        writer.close();
    }

    private void indexDocuments(IndexWriter writer, Path docDir) throws IOException {
        if(Files.isDirectory(docDir)) {
            Files.walkFileTree(docDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    indexDocument(writer, file, attrs.lastModifiedTime().toMillis());
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void indexDocument(IndexWriter writer, Path file, long lastModified) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("path", file.toString(), Field.Store.YES));
        doc.add(new LongPoint("modified", lastModified));
        doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Field.Store.YES));
        writer.updateDocument(new Term("path", file.toString()), doc);
    }

}

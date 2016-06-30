package com.solar.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class LuceneReader {

    private Directory directory;

    private Analyzer  analyzer;

    public LuceneReader(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void read() throws IOException, ParseException {
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "fileName", analyzer);
        Query query = parser.parse("text");
        ScoreDoc[] hits = searcher.search(query, null, 1000).scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = searcher.doc(hits[i].doc);
            System.out.println(hitDoc.get("fieldname"));
        }
        reader.close();
        directory.close();
    }
}

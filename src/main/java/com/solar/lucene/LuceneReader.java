package com.solar.lucene;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.solar.protocol.SearchRequest;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class LuceneReader {

    private Directory directory;

    private Analyzer  analyzer;

    public LuceneReader(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void read(SearchRequest searchRequest) throws IOException, ParseException {
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = buildQuery(searchRequest);
        ScoreDoc[] scoreDocs = searcher.search(query, null, 1000).scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            List<IndexableField> fields = document.getFields();
            for (IndexableField indexableField : fields) {
                System.out
                    .println(indexableField.name() + " value：" + indexableField.stringValue());
            }
        }
        reader.close();
        directory.close();
    }

    private Query buildQuery(SearchRequest searchRequest) {
        Map<String, String> querys = searchRequest.getQuery();
        Query query = null;
        for (Entry<String, String> entry : querys.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            QueryParser parser = new QueryParser(Version.LUCENE_46, field, analyzer);
            try {
                query = parser.parse(value);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return query;
    }

    public static void main(String[] args) {
        try {
            Map<String, String> queryMap = new HashMap<String, String>();
            queryMap.put("name", "123");
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setQuery(queryMap);
            Directory directory = FSDirectory.open(new File("/Users/mac/index"));
            Analyzer analyzer = new PaodingAnalyzer();
            LuceneReader reader = new LuceneReader(directory, analyzer);
            reader.read(searchRequest);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}

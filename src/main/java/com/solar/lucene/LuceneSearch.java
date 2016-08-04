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
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.protocol.SearchRequest;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class LuceneSearch implements SolarSearch {

    private static final Logger logger = LoggerFactory.getLogger(LuceneSearch.class);

    private Directory           directory;

    private Analyzer            analyzer;

    private IndexSearcher       searcher;

    public LuceneSearch(Directory directory) {
        this.directory = directory;
        DirectoryReader reader = null;
        try {
            reader = DirectoryReader.open(directory);
        } catch (IOException e) {
            logger.error("", e);
        }
        this.searcher = new IndexSearcher(reader);
    }

    //之后废弃
    public LuceneSearch(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
        DirectoryReader reader = null;
        try {
            reader = DirectoryReader.open(directory);
        } catch (IOException e) {
            logger.error("", e);
        }
        this.searcher = new IndexSearcher(reader);
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
            LuceneSearch reader = new LuceneSearch(directory, analyzer);
            reader.read(searchRequest);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public TopDocs search(Query query, int maxHitsPerSearch) {
        TopDocs result = null;
        try {
            result = searcher.search(query, maxHitsPerSearch);
        } catch (IOException e) {
            logger.error("", e);
        }
        for (ScoreDoc scoreDoc : result.scoreDocs) {
            Document document = null;
            try {
                document = searcher.doc(scoreDoc.doc);
            } catch (IOException e) {
                logger.error("", e);
            }
            if (document != null) {
                List<IndexableField> fields = document.getFields();
                for (IndexableField indexableField : fields) {
                    System.out
                        .println(indexableField.name() + " value：" + indexableField.stringValue());
                }
            }
        }
        return result;
    }

    public TopDocs search(Query query, Filter filter, int maxHitsPerSearch) {
        TopDocs result = null;
        try {
            result = searcher.search(query, filter, maxHitsPerSearch);
        } catch (IOException e) {
            logger.error("", e);
        }
        return result;
    }
}

package com.solar.lucene;

import java.io.IOException;
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
import org.apache.lucene.util.Version;

import com.solar.protocol.SearchQuery;

public class LuceneReader {

    private Directory directory;

    private Analyzer  analyzer;

    public LuceneReader(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void read(SearchQuery searchQuery) throws IOException, ParseException {
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
//        QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "fileName", analyzer);
//        Query query = parser.parse("text");
        Query query = buildQuery(searchQuery);
        ScoreDoc[] scoreDocs = searcher.search(query, null, 1000).scoreDocs;
        for(ScoreDoc scoreDoc:scoreDocs){
        	Document document = searcher.doc(scoreDoc.doc);
        	List<IndexableField> fields = document.getFields();
        	for(IndexableField indexableField:fields){
        		System.out.println(indexableField.name()+"value："+indexableField);
        	}
        }
        reader.close();
        directory.close();
    }
    
    private Query buildQuery(SearchQuery searchQuery){
    	Map<String,String> querys = searchQuery.getQuery();
    	Query query = null;
    	for(Entry<String,String> entry:querys.entrySet()){
    		String field = entry.getKey();
    		String value = entry.getValue();
    		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, field, analyzer);
    		try {
				query = parser.parse(value);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return query;
    }
}

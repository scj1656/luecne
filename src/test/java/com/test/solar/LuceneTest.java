package com.test.solar;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import com.solar.annotation.AnnotationExtracter;
import com.solar.lucene.LuceneReader;
import com.solar.lucene.LuceneWriter;

public class LuceneTest extends TestCase{
	
	private Directory directory;
    private Analyzer  analyzer;
    private String path = "";
    
    @Before
    public void setUp(){
    	try {
			directory = FSDirectory.open(new File(path));
			analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
			LuceneWriter writer = new LuceneWriter(directory, analyzer);
			writer.createIndex(new AnnotationExtracter(Object.class));
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void searchTest(){
    	LuceneReader reader = new LuceneReader(directory, analyzer);
//			reader.read(searchQuery);
    }
}

package com.test.solar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class LuceneTest extends TestCase {

    private Directory directory;
    private Analyzer  analyzer;
    private String    path = "/Users/apple/luecene/index";

    @Before
    public void setUp() {
        //    	try {
        //    		ProductIndex product = new ProductIndex("dioa", "d3s");
        //			directory = FSDirectory.open(new File(path));
        //			analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        //			LuceneWriter writer = new LuceneWriter(directory, analyzer);
        //			writer.createIndex(new AnnotationExtracter(ProductIndex.class));
        //    	} catch (IOException e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}
    }

    @Test
    public void searchTest() {
        //    	LuceneReader reader = new LuceneReader(directory, analyzer);
        //			reader.read(searchQuery);
    }
}

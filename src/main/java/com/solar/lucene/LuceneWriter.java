package com.solar.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class LuceneWriter {

    private Directory directory;
    private Analyzer  analyzer;

    public LuceneWriter() {

    }

    public LuceneWriter(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void createIndex() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        Document doc = new Document();
        String text = "this is lucene test";
        doc.add(new Field("fileName", text, TextField.TYPE_STORED));
        writer.addDocument(doc);
        writer.close();
    }
}

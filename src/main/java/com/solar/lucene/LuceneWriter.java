package com.solar.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import com.alibaba.fastjson.JSON;
import com.solar.annotation.AnnotationExtracter;
import com.solar.annotation.AnnotationField;

public class LuceneWriter {

    private Directory directory;
    private Analyzer  analyzer;

    public LuceneWriter() {

    }

    public LuceneWriter(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void createIndex(AnnotationExtracter extracter) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
//        Document doc = new Document();
//        String text = "this is lucene test";
//        doc.add(new Field("fileName", text, TextField.TYPE_STORED));
//        writer.addDocument(doc);
        Document doc = getLuceneDocument(extracter);
        writer.updateDocument(null, doc);
        writer.close();
    }

    /**
     * 解析数据生成document文件，注意以后再做自定义document
     * @author xiaojie
     * 2016年7月1日 下午4:11:53
     * @param extracter
     * @return
     */
    public Document getLuceneDocument(AnnotationExtracter extracter) {
        Document document = new Document();
        List<AnnotationField> annotationFields = extracter.getAnnotatedFields();
        for (AnnotationField annotationField : annotationFields) {
            document.add(new Field(annotationField.getFieldName(),
                JSON.toJSONString(annotationField.getFieldValue()), TextField.TYPE_STORED));
        }
        return document;
    }
}

package com.solar.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.alibaba.fastjson.JSON;
import com.solar.annotation.AnnotationExtracter;
import com.solar.annotation.AnnotationField;
import com.solar.constants.FieldConstants;
import com.solar.model.SkuIndex;

public class LuceneWriter {

    private Directory directory;
    private Analyzer  analyzer;

    public LuceneWriter() {

    }

    public LuceneWriter(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    private void createIndex(AnnotationExtracter extracter) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        Document doc = getLuceneDocument(extracter);
        if (doc.getField(FieldConstants.FIELD_ID) != null) {
            Term term = new Term(FieldConstants.FIELD_ID,
                doc.getField(FieldConstants.FIELD_ID).stringValue());
            writer.updateDocument(term, doc);
        } else {
            writer.addDocument(doc);
        }
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

    public static void main(String[] args) {
        SkuIndex sku = new SkuIndex(1, "test", "dioa");
        Directory directory;
        try {
            directory = FSDirectory.open(new File("/Users/mac/index"));
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            LuceneWriter writer = new LuceneWriter(directory, analyzer);
            writer.createIndex(new AnnotationExtracter(sku));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

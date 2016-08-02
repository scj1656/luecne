package com.solar.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.solar.annotation.AnnotationExtracter;
import com.solar.annotation.AnnotationField;
import com.solar.common.constants.FieldConstants;
import com.solar.model.ByteValue.ValueType;
import com.solar.model.IndexType;
import com.solar.model.SkuIndex;
import com.solar.model.SolarDocument;
import com.solar.model.SolarField;

public class LuceneWriter implements SolarWriter {

    private static final Logger logger      = LoggerFactory.getLogger(LuceneWriter.class);

    private Directory           directory;
    private Analyzer            analyzer;
    private IndexWriter         indexWriter = null;

    public LuceneWriter(Directory directory, Analyzer analyzer) {
        this.directory = directory;
        this.analyzer = analyzer;
        this.indexWriter = createIndexWriter(directory, analyzer);
    }

    private IndexWriter createIndexWriter(Directory directory, Analyzer analyzer) {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);//默认是这个
        try {
            if (IndexWriter.isLocked(directory)) {
                return null;
            }
            IndexWriter indexWriter = new IndexWriter(directory, config);
            return indexWriter;
        } catch (IOException e) {
            try {
                String[] fileNames = directory.listAll();
                for (String file : fileNames) {
                    directory.deleteFile(file);
                }
            } catch (IOException e1) {
                logger.error("Delete index directory fail", e1);
                //                throw new LuceneException("Delete index directory fail", e1);
            }
            IndexWriter writer = null;
            try {
                writer = new IndexWriter(directory, config);
            } catch (IOException e1) {
                logger.error("", e);
            }
            logger.error("", e);
            return writer;
        }

    }

    public void addDocument(SolarDocument doc) {
        Document document = exchangeDocument(doc);
        try {
            indexWriter.addDocument(document);
        } catch (IOException e) {
            logger.error("index add failure", e);
        }
    }

    public void updateDocument(SolarDocument doc) {
        Document document = exchangeDocument(doc);
        if (document.getField(FieldConstants.FIELD_ID) != null) {
            Term term = new Term(FieldConstants.FIELD_ID,
                document.getField(FieldConstants.FIELD_ID).stringValue());
            try {
                indexWriter.updateDocument(term, document);
            } catch (IOException e) {
                logger.error("index update failure", e);
            }
        } else {
            addDocument(doc);
        }
    }

    public void deleteDocument(SolarDocument doc) {
        Document document = exchangeDocument(doc);
        if (document.getField(FieldConstants.FIELD_ID) != null) {
            Term term = new Term(FieldConstants.FIELD_ID,
                document.getField(FieldConstants.FIELD_ID).stringValue());
            try {
                indexWriter.deleteDocuments(term);
            } catch (IOException e) {
                logger.error("index delete failure", e);
            }
        }
    }

    public Document exchangeDocument(SolarDocument solarDoc) {
        Document doc = new Document();
        List<SolarField> solarFields = solarDoc.getField();
        if (!solarFields.isEmpty()) {
            for (SolarField solarField : solarFields) {
                IndexType indexType = solarField.getIndexType();
                ValueType valueType = solarField.getValue().getValueType();

                FieldType luceneFieldType = new FieldType();
                luceneFieldType.setIndexed(indexType.isIndexed());
                luceneFieldType.setStored(indexType.isStored());
                luceneFieldType.setTokenized(indexType.isTokenized());

                switch (valueType) {
                    case INT:
                        luceneFieldType.setNumericType(NumericType.INT);
                        doc.add(new IntField(solarField.getName(),
                            solarField.getValue().getIntValue(), luceneFieldType));
                        break;
                    case LONG:
                        luceneFieldType.setNumericType(NumericType.LONG);
                        doc.add(new LongField(solarField.getName(),
                            solarField.getValue().getLongValue(), luceneFieldType));
                        break;
                    case FLOAT:
                        luceneFieldType.setNumericType(NumericType.FLOAT);
                        doc.add(new FloatField(solarField.getName(),
                            solarField.getValue().getFloatValue(), luceneFieldType));
                        break;
                    case DOUBLE:
                        luceneFieldType.setNumericType(NumericType.DOUBLE);
                        doc.add(new DoubleField(solarField.getName(),
                            solarField.getValue().getDoubleValue(), luceneFieldType));
                        break;
                    case STRING:
                        doc.add(new Field(solarField.getName(),
                            solarField.getValue().getStringValue(), luceneFieldType));
                        break;
                }
            }
        }
        return doc;
    }

    private void createIndex(AnnotationExtracter extracter) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
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
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
            LuceneWriter writer = new LuceneWriter(directory, analyzer);
            writer.createIndex(new AnnotationExtracter(sku));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void commit() {
        try {
            indexWriter.commit();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

}

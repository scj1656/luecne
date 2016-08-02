package com.solar.common.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class AnalyzerFactory {

    private static AnalyzerFactory analyzerFactory = null;

    private AnalyzerFactory() {

    }

    public static AnalyzerFactory getInstance() {
        synchronized (AnalyzerFactory.class) {
            if (analyzerFactory == null) {
                analyzerFactory = new AnalyzerFactory();
            }
            return analyzerFactory;
        }
    }

    public Analyzer getAnalyzer(String analyzerName) {
        Analyzer analyzer = null;
        if ("paoding".equals(analyzerName)) {
            analyzer = new PaodingAnalyzer();
        } else {
            analyzer = new StandardAnalyzer(Version.LUCENE_46);
        }
        return analyzer;
    }
}

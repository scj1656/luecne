package com.solar.handler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.annotation.SolarHandler;
import com.solar.common.config.AnalyzerFactory;
import com.solar.common.config.SolarConfig;
import com.solar.lucene.LuceneWriter;
import com.solar.lucene.SolarWriter;
import com.solar.model.SolarDocument;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;
import com.solar.protocol.UpdateRequest;

@SolarHandler
public class UpdateHandler extends RequestHandler {

    private static final Logger logger         = LoggerFactory.getLogger(UpdateHandler.class);

    private static final String UPDATE_HANDLER = "update";

    private SolarWriter         solarWriter;

    public String getName() {
        return UPDATE_HANDLER;
    }

    @Override
    public void handler(SolarRequest solarRequest, SolarResponse solarResponse) {
        UpdateRequest updateRequest = (UpdateRequest) solarRequest;
        Analyzer anlyzer = AnalyzerFactory.getInstance()
            .getAnalyzer(updateRequest.getAnalyzerName());
        Directory directory = null;
        try {
            System.out.println(SolarConfig.DEFAULT.getIndexDir());
            directory = FSDirectory.open(new File(SolarConfig.DEFAULT.getIndexDir()));
        } catch (IOException e) {
            logger.error("", e);
        }
        solarWriter = new LuceneWriter(directory, anlyzer);
        List<SolarDocument> documents = updateRequest.exportData();
        for (SolarDocument document : documents) {
            solarWriter.updateDocument(document);
        }
        solarWriter.commit();

    }

}

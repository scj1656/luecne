package com.solar.lucene;

import com.solar.model.SolarDocument;

public interface SolarWriter {

    void addDocument(SolarDocument doc);

    void updateDocument(SolarDocument doc);

    void deleteDocument(SolarDocument doc);

}
